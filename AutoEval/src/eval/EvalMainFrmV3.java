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
    		//eroor sign, need to reinput 
    		int errorSign=1;
    		while(errorSign>0)  {
	    		myInput = new Scanner(System.in);
	    	    System.out.println("Please Enter 1 (cmd) or 2 (windows) mode) ");
	    	    // String input
	    	    String mode = myInput.nextLine();
	
	    	    switch(mode) {
		    	    case "1":
		    	    	// Cmd Mode
		    	    	System.out.println("Cmd Mode, Please Input Eval Soft and  Data Directory");
		    	    	myInput = new Scanner(System.in);
		    	    	//remove leading and trailing spacese
		    	    	//runEvalCmd(myInput.nextLine().trim(),myInput.nextLine().trim());		    	    	
		    	    	
		    	    	
		    	    	//Win10
		    	    	runEvalCmd("C:\\Users\\jacks\\Documents\\temp\\WinEval","C:\\Users\\jacks\\Documents\\New\\0627");
		    	    	
		    	    	//  C:\Users\jacks\Documents\temp\WinEval   	    	
		    	    	//  C:\Users\jacks\Documents\New\0627
		    	    	
		    	    	
		    	    	//Linux
		    	    	//   /Users/jian/Documents/GitHub/MakLab-012-CSTQ-AutoEval-Modify/AutoEval/src/eval/MacEval	   	    	   
		    	    	//   /Users/jian/Downloads/1data-lab/new/0623-UVA-NL-Hero-Linux
		    	    	//runEvalCmd("/Users/jian/Documents/GitHub/MakLab-012-CSTQ-AutoEval-Modify/AutoEval/src/eval/MacEval", "/Users/jian/Downloads/1data-lab/new/0623-UVA-NL-Hero-Linux");
		    	        //exit
		    	    	errorSign=0;
		    	    	break;
		    	    case "2":
		    	    	// Windows Mode
			    		System.out.println("Windows Mode:");
			    		createWindow();
			    		errorSign=0;
			    		break;
			    		
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
    private static ArrayList<String> fileNamePreProcessed(File file) {

        ArrayList<String> datasetNamesList=new ArrayList<String>();
        file.list();
        
        System.out.println("fileNamePreProcessed");
        //list only directory
        String[] directories = file.list(new FilenameFilter() {
        	  @Override
        	  public boolean accept(File current, String name) {
        		  
        	    return new File(current, name).isDirectory();
        	  }
        	});
        //System.out.println("Data Dirctory Includes : "+Arrays.toString(directories));
       
    
        // remove log and other directories
        for(int listIndex=0;listIndex<directories.length;listIndex++) {
        	if(!directories[listIndex].contains("logdir")) {
        		datasetNamesList.add(directories[listIndex]);
        	}
        	
        }
        
        //datasetNamesList= new ArrayList<>(Arrays.asList(directories));
        
   
        System.out.println(Arrays.deepToString(datasetNamesList.toArray()));

       
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
                datasetNamesList=fileNamePreProcessed(fileDataDirector);


                /*
                Generate Run Cmd
                 */
                String absPathEval = labelEvalSet;
                String absPathData = labelDataSet;

                //Run the Evaluation Software
                String[] evalcmd = {"DETMeasure", "SEGMeasure", "TRAMeasure"};
                String[] sequence = {"01", "02"};

                String splStrOS = "/";
                if (EvalUtil.isWindows())
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
                
                
                // Get now time
                Date mydate=new Date();
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd-hh-mm");
                
                
                //Create Logs Dir ("yyyy-MM-dd-hh-mm")
                //String LogsDir=absPathData+splStrOS+ft.format(mydate).substring(0,16)+"-logdir";
                String LogsDir=absPathData+splStrOS+ft.format(mydate)+"-logdir";
                //Initial of log Dir
                EvalUtil.creatLogDir(LogsDir);
                
                //Save Results
                evaResultNames=LogsDir+splStrOS+"Result-"+ft.format(mydate)+"-eval.csv";
                
                 
                //cmd chown
                Process p1 ;
                //cmd run all sh files
                Process p2 ;
                //cmd: open result directory
                Process p3 ;
                
                for (int nameIdx = 0; nameIdx < datasetNamesList.size(); nameIdx++) {
                    for (int evalcmdIdx = 0; evalcmdIdx < 3; evalcmdIdx++){
                        for (int seqIdx = 0; seqIdx < 2; seqIdx++) {                  
                            // Generate SH
                            //String evalShNames =absPathEval+splStrOS+datasetNamesList.get(nameIdx).toString()+"evalSh.sh";
                        	mydate=new Date();
                        	 
                            

                            //System.out.println("Current Date: " + ft.format(mydate));
                            

                            //Save Sh cmd files
                            if(EvalUtil.isWindows()){
                            	evalShNames = LogsDir+splStrOS+ft.format(mydate)+"-Sh-"+numCmdFiles+"-eval.bat";
                            }
                            else {
                            	evalShNames = LogsDir+splStrOS+ft.format(mydate)+"-Sh-"+numCmdFiles+"-eval.sh";
                            }
                            //Save Log files
                            evalLogNames=LogsDir+splStrOS+ft.format(mydate)+"-logs-"+numCmdFiles+"-eval.txt";

                            	
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


                            if(EvalUtil.isWindows()){
                                outputPrintStream.print(cmdTotlStr+"\r\n");

                            }
                            else if (EvalUtil.isLinux()) {
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
                            
                            

                            
                            
                            
                            /*
                            // Run Cmd
                            //
                             */
                            
                            

                            System.out.println("Creating Process...");                        
                            rt.freeMemory();        
               
                            // Linux or Windows need chown
                            if(!EvalUtil.isWindows()) {
                            	String[] cmdChown={"chown", "777",evalShNames};
                            	p1 = rt.exec(cmdChown);
                            	p1.waitFor();
                            	
                                //String[] cmdEvalRun={ "/bin/bash", evalShNames};
                                String[] cmdEvalRun={ "sh", evalShNames};
                                p2 = rt.exec(cmdEvalRun);
                                //Wait Over
                                System.out.println("Waiting over for the"+numCmdFiles+" sh running.");
                                p2.waitFor();
                            }
                            else if(EvalUtil.isWindows()) {
                                //if Win10
                                String[] cmdEvalRun_Win={"cmd","/c",evalShNames};
                                p2 = rt.exec(cmdEvalRun_Win);
                                //Wait Over
                                System.out.println("Waiting over for the"+numCmdFiles+" sh running.");
                                p2.waitFor();
                            }
                            


                            
                            // count ++
                            numCmdFiles++;



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
                            
                            //save the first 4 column
                            // col1: eval: DET, SEG, TRA
                            // col2: datasetname=filename
                            // col3: sequence: 01,02
                            // col4: other parameter: default 3
                        

                            //FileUtils.writeStringToFile(fileSaveResult, "Hello File", forName("UTF-8"));
                            System.out.print(lastLine);
                            System.out.print("\n");
                            //outputPrintStreamResult.print(lastLine);
                            //outputPrintStreamResult.print("\n");
                            
                            
                            // all log out
                            if(alloutput){
                                resultOutStr+=lastLine;
                                resultOutStr+="\n";
                            }
                            // only part  - good result message out
                            else {
                            	
                            	// find measure: 0.99998 ... save
                            	int findIndex=lastLine.lastIndexOf("measure");
                            
                            	if(findIndex>=0 && lastLine.substring(findIndex,findIndex+11).equals("measure: 0.")){

                                    
                                    for (int parameterIndx=0;parameterIndx<4;parameterIndx++)
                                    {
                                        System.out.print(mycmd[nameIdx][evalcmdIdx][seqIdx][parameterIndx]);
                                        resultOutStr+=mycmd[nameIdx][evalcmdIdx][seqIdx][parameterIndx];
                                        //if at end no comma printed
                                        System.out.print(",");
                                        //outputPrintStreamResult.print(",");
                                        resultOutStr+=",";

                                    }
                                    
                            		//System.out.println(lastLine.substring(findIndex,findIndex+2));
                                    findIndex=lastLine.lastIndexOf("0.");
                                    //Get the last 8 number is the result
                                    resultOutStr+=lastLine.substring(findIndex,findIndex+8);
                                    resultOutStr+="\n";
                            	}
                            	
                            	
                            }
                            

                            
                         

                        }
                    }
                }
                System.out.println(cmdTotlStr);

                System.out.println("All sh run finished. Save Result");
                outputPrintStreamResult.print(resultOutStr);
                
                System.out.println("Save Result done");
                
                
                //String[] cdCmd={"cd",LogsDir};
                //p3 = rt.exec(cdCmd);
                //Wait Over
                System.out.println("All Done. Enjoy.");
                
               
            
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
