package eval;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class EvalMainFrmV3 {
    // Global datasetNames List
    //public static ArrayList<String> datasetNamesList=new ArrayList<String>();

    public static void main(String[] args) {
    	Scanner myInput=null;
    	try {
    		
    		while(true) {
	    		myInput = new Scanner(System.in);
	    	    System.out.println("Please Enter 1 (cmd) or 2 (windows) mode) ");
	    	    // String input
	    	    String mode = myInput.nextLine();
	
	    	    switch(mode) {
		    	    case "1":
		    	    	// Cmd Mode
		    	    	System.out.println("Cmd Mode, Please Input Eval Soft and  Data Directory");
		    	    	myInput = new Scanner(System.in);
		    	    	
		    	    	//   runEvalCmd(myInput.nextLine().toString(),myInput.nextLine().toString());		    	    	
		    	    	//   /Users/jian/Documents/GitHub/MakLab-012-CSTQ-AutoEval-Modify/AutoEval/src/eval/MacEval	   	    	   
		    	    	//   /Users/jian/Downloads/1data-lab/new/0623-UVA-NL-Hero-Linux
		    	    	
		    	    	runEvalCmd("/Users/jian/Documents/GitHub/MakLab-012-CSTQ-AutoEval-Modify/AutoEval/src/eval/MacEval", "/Users/jian/Downloads/1data-lab/new/0623-UVA-NL-Hero-Linux");
		    	        //exit
		    	    	System.exit(0);
		    	    case "2":
		    	    	// Windows Mode
			    		System.out.println("Windows Mode:");
			    		createWindow();
			    		//exit
			    		System.exit(0);
		    	    default:
		    	    	System.out.println("Input Error");
		    	    	
		    	}
    		}

    	}
    	catch(Exception e) {
    		System.out.println(e.toString());
    	}
    	finally
    	{
            if (myInput != null) {
            	myInput.close();
            }

    	}
    }


    private static void createWindow() {
        JFrame frame = new JFrame("Swing Tester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI(frame);
        frame.setSize(600, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createUI(final JFrame frame){
        JPanel panelResult = new JPanel();
        LayoutManager layout = new FlowLayout();
        panelResult.setLayout(layout);

        JPanel panelEval = new JPanel();
        LayoutManager layoutEval = new FlowLayout();
        panelEval.setLayout(layoutEval);

        JPanel panelData = new JPanel();
        LayoutManager layoutData = new FlowLayout();
        panelData.setLayout(layoutData);

        JButton buttonEva = new JButton("Please Choose Evaluation Software Location");
        final JLabel labelEval = new JLabel();
        JButton buttonData = new JButton("Please Choose Data Location");
        final JLabel labelData = new JLabel();

        JButton buttonRun = new JButton("Run");
        final JLabel labelRun = new JLabel("Run Result are as Following");


        /**
         * Layout
         */

        panelEval.add(buttonEva);
        panelEval.add(labelEval);

        panelData.add(buttonData);
        panelData.add(labelData);

        panelResult.add(buttonRun);
        panelResult.add(labelRun);

        JSplitPane spFile=new JSplitPane(JSplitPane.VERTICAL_SPLIT,panelEval,panelData);
        spFile.setDividerLocation(100);

        JSplitPane spResult=new JSplitPane(JSplitPane.VERTICAL_SPLIT,spFile,panelResult);
        spResult.setDividerLocation(200);

        frame.getContentPane().add(spResult, BorderLayout.CENTER);


        /**
         * action
         */
        buttonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    runEvalCmd(labelEval.getText().toString(), labelData.getText().toString());
                    //Hero Linux
                	//runEvalCmd("/home/users/jzhao/code/Alldata/LinuxEval", "/home/users/jzhao/code/Alldata/CSTQ/Raw");
                	//runEvalCmd("/Users/jian/Documents/Data/Cell_Tracking_Challenge/EvalMac", "/Users/jian/Documents/Data/Cell_Tracking_Challenge/Training");
                
                } catch (IOException ex) {
                    throw new RuntimeException(ex);

                }
            }
        });

        buttonEva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    labelEval.setText(file.getAbsolutePath());
                }else{
                    labelEval.setText("Open command canceled");
                }
            }
        });


        buttonData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fileChooser.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION){
                    File file = fileChooser.getSelectedFile();
                    labelData.setText(file.getAbsolutePath());
                    //fileNamePreDeall(file);
                }else{
                    labelData.setText("Open command canceled");
                }
            }
        });
    }

    /** 
     * Get all datasetname from current director
     * @param file
     * @return
     */
    private static ArrayList<String> fileNamePreProcessing(File file) {

        ArrayList<String> datasetNamesList=new ArrayList<String>();
        file.list();
        
        
        //list only directory
        String[] directories = file.list(new FilenameFilter() {
        	  @Override
        	  public boolean accept(File current, String name) {
        	    return new File(current, name).isDirectory();
        	  }
        	});
        //print directory
        System.out.println("Data Dirctory Includes : "+Arrays.toString(directories));
        	  	
        datasetNamesList= new ArrayList<>(Arrays.asList(directories));
       
        return datasetNamesList;
    }

    private static void runEvalCmd(String labelEvalSet, String labelDataSet) throws IOException {

        FileOutputStream outputStream= null;
        PrintStream outputPrintStream = null;

        FileOutputStream outputStreamResult  = null;
        PrintStream outputPrintStreamResult = null;

        BufferedReader bufferedReader = null;
        FileReader fileReader= null;

        Runtime rt = Runtime.getRuntime();
        try {
            if (labelEvalSet == null || labelEvalSet.equals("") || labelDataSet ==null || labelDataSet.equals("")) {
                String errorMsg = "Please Check Evaluation Software Path to Double Sure!";
                //System.out.println(errorMsg);
                JOptionPane.showMessageDialog(null, errorMsg);
            } else {

                //Pre Process Data Directory and get all datasets name
                //Creat File from Date Directory
                ArrayList<String> datasetNamesList=new ArrayList<String>();
                File fileDataDirector=new File(labelDataSet);
                datasetNamesList=fileNamePreProcessing(fileDataDirector);


                /*
                Generate Run Cmd
                 */
                String absPathEval = labelEvalSet;
                String absPathData = labelDataSet;

                //Run the Evaluation Software
                String[] evalcmd = {"DETMeasure", "SEGMeasure", "TRAMeasure"};
                String[] sequence = {"01", "02"};

                String splStrOS = "/";
                if (EvalOSUtil.isWindows())
                {
                	splStrOS="\\";
                }

                
                //Command such as 'DETMeasure datasetName 01 3 >> evalLogNames.txt'
                //[0] datasetName: PhC-C2DL-PSC, Fluo-N2DH-SIM+
                //[1] evaluate fuction: DET, SEG, TRA
                //[2] sequence: 01, 02
                //[3] cmd has 6 parameter. 
                String[][][][] mycmd = new String[datasetNamesList.size()][3][2][6];
                String cmdTotlStr= "";


                int numCmdFiles=0;
                // full ssh cmd to one string
                String fullCmdtoStr="";
                // result output content
                String resultOutStr="";
                //save all result data
                boolean alloutput=false;
                
                //Save SH name
                String evalShNames ="";
                //Save Log name for each SH file
                String evalLogNames="";
                //Save Final Results name
                String evaResultNames="";
                
                for (int nameIdx = 0; nameIdx < datasetNamesList.size(); nameIdx++) {
                    for (int evalcmdIdx = 0; evalcmdIdx < 3; evalcmdIdx++){
                        for (int seqIdx = 0; seqIdx < 2; seqIdx++) {                  
                            // Generate SH
                            //String evalShNames =absPathEval+splStrOS+datasetNamesList.get(nameIdx).toString()+"evalSh.sh";
                        	Date mydate=new Date();
                        	 
                            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd-hh-mm");

                            System.out.println("Current Date: " + ft.format(mydate));
                            
                            
                            //Create Logs Dir
                            String LogsDir=absPathData+splStrOS+ft.format(mydate).substring(0,15)+"-logdir";
                            //Initial of log Dir
                            EvalOSUtil.creatLogDir(LogsDir);
                            
                            evalShNames =LogsDir+splStrOS+ft.format(mydate)+"-Sh-"+numCmdFiles+"-eval.sh";
                            //Save Log
                            evalLogNames=LogsDir+splStrOS+ft.format(mydate)+"-logs-"+numCmdFiles+"-eval.txt";
                            //Save Results
                            evaResultNames=LogsDir+splStrOS+ft.format(mydate)+"-Result"+"-eval.txt";
                            

                            	
                            outputStream= new FileOutputStream(evalShNames);
                            outputPrintStream = new PrintStream(outputStream);


                            cmdTotlStr="";
                            //parameter 00 DET, SEG, TRA Tools
                            mycmd[nameIdx][evalcmdIdx][seqIdx][0] = absPathEval + splStrOS + evalcmd[evalcmdIdx];
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][0]+" ";
                            
                            //parameter 01 Data Directory
                            mycmd[nameIdx][evalcmdIdx][seqIdx][1] = absPathData + splStrOS + datasetNamesList.get(nameIdx);
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][1]+" ";
                            
                            //parameter 02 Sequence 01, or 02
                            mycmd[nameIdx][evalcmdIdx][seqIdx][2] = sequence[seqIdx];
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][2]+" ";
                            
                            //parameter 03, default is 3
                            mycmd[nameIdx][evalcmdIdx][seqIdx][3] = "3";
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][3]+" ";
                            
                            //parameter 04  save each SH file result >> each log
                            mycmd[nameIdx][evalcmdIdx][seqIdx][4] = ">>";
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][4]+" ";
                            
                            //parameter 05  each log name
                            mycmd[nameIdx][evalcmdIdx][seqIdx][5] = evalLogNames;
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][5];

                            //System.out.println(cmdTotlStr);

                            fullCmdtoStr=fullCmdtoStr+cmdTotlStr+"\n";


                            if(EvalOSUtil.isWindows()){
                                outputPrintStream.print(cmdTotlStr+"\r\n");

                            }
                            else if (EvalOSUtil.isLinux()) {
                                outputPrintStream.print(cmdTotlStr+"\n");
                                //Generate run cmd
                                File myObj = new File(evalLogNames);
                                if (myObj.createNewFile()) {
                                       System.out.println("eval log created: " + myObj.getName());
                                 } else {
                                       System.out.println("eval log already exists.");
                                 }
                            }
                            	
                            else{
                            	//Mac
                                outputPrintStream.print(cmdTotlStr+"\n");
                            }
                            
                            
                            // count ++
                            numCmdFiles++;
                            
                            
                            
                            /*
                            // Run Cmd
                            //
                             */
                            
                            String[] cmdChown={"chown", "777",evalShNames};
                            //String[] cmdEvalRun={ "/bin/bash", evalShNames};
                            String[] cmdEvalRun={ "sh", evalShNames};
                            System.out.println("Creating Process...");                        
                            rt.freeMemory();        
                            Process p1 = rt.exec(cmdChown);
                            Process p2 = rt.exec(cmdEvalRun);
                            // cause this process to stop until process p is terminated
                            p1.waitFor();
                            p2.waitFor();
                            // when you manually close notepad.exe program will continue here
                            System.out.println("Waiting over.");
                            




                            //rt.exit(0);

                            /*
                             * Post Data Processing
                             * */
                            // Read Evaluation Log Generate Result Data
                            fileReader= new FileReader(evalLogNames);
                            bufferedReader = new BufferedReader(fileReader);
                            String lastLine = "";
                            String currentLogLine="";
                            //ArrayList<String> cmdlogList=new ArrayList<String>();
                            while ((currentLogLine = bufferedReader.readLine()) != null) {
                                //System.out.println(currentLogLine);
                            	//lastLine=currentLogLine.substring(0,21);
                                lastLine=currentLogLine;
                                System.out.println("last line is :"+lastLine);
                            }


//                            //Save Result File
                            outputStreamResult=new FileOutputStream(evaResultNames);
                            outputPrintStreamResult = new PrintStream(outputStreamResult);
                            mycmd[nameIdx][evalcmdIdx][seqIdx][0] = evalcmd[evalcmdIdx];
                            mycmd[nameIdx][evalcmdIdx][seqIdx][1] = datasetNamesList.get(nameIdx);
                            for (int parameterIndx=0;parameterIndx<4;parameterIndx++)
                            {
                                System.out.print(mycmd[nameIdx][evalcmdIdx][seqIdx][parameterIndx]);
                                resultOutStr+=mycmd[nameIdx][evalcmdIdx][seqIdx][parameterIndx];
                                //if at end no comma printed
                                System.out.print(",");
                                //outputPrintStreamResult.print(",");
                                resultOutStr+=",";

                            }
                            //FileUtils.writeStringToFile(fileSaveResult, "Hello File", forName("UTF-8"));
                            System.out.print(lastLine);
                            System.out.print("\n");
                            //outputPrintStreamResult.print(lastLine);
                            //outputPrintStreamResult.print("\n");
                            
                            
                            // all log out
                            if(alloutput){
                                resultOutStr+=lastLine;
                            }
                            // only part  - good result message out
                            else {
                            	
                            	// find measure: 0.99998 ... save
                            	int findIndex=lastLine.lastIndexOf("0.");
                            
                            	if(findIndex>=0 && lastLine.substring(findIndex,findIndex+2).equals("0.")){
                            		System.out.println(lastLine.substring(findIndex,findIndex+2));
                                    resultOutStr+=lastLine;
                            	}
                            	
                            }
                            
                            resultOutStr+="\n";

                        }
                    }
                }
                System.out.println(cmdTotlStr);

                outputPrintStreamResult.print(resultOutStr);
               
            
            }

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        finally {
            if (outputPrintStream != null) {
                outputPrintStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }

        }
    }




   
      
}
