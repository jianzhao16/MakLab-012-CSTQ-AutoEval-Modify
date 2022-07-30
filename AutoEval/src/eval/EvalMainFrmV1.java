package eval;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;


public class EvalMainFrmV1 {
    // Global datasetNames List
    //public static ArrayList<String> datasetNamesList=new ArrayList<String>();

    public static void main(String[] args) {
        createWindow();
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
            //@Override
            public void actionPerformed(ActionEvent e) {
                try {
                    runEvalCmd("/Users/jian/Documents/Data/Cell_Tracking_Challenge/EvalMac", "/Users/jian/Documents/Data/Cell_Tracking_Challenge/Training");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);

                }
            }
        });

        buttonEva.addActionListener(new ActionListener() {
            //@Override
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

    private static ArrayList<String> fileNamePreProcessing(File file) {
        //get all datasetname
        ArrayList<String> datasetNamesList=new ArrayList<String>();
        file.list();
        datasetNamesList= new ArrayList<>(Arrays.asList(file.list()));
        int removeIdx=datasetNamesList.indexOf(".DS_Store");
        if (removeIdx!=-1) {
            datasetNamesList.remove(removeIdx);
        }

        System.out.println(file.list().toString());
        return datasetNamesList;
    }

    private static void runEvalCmd(String labelEvalSet, String labelDataSet) throws IOException {

        FileOutputStream outputStream= null;
        PrintStream outputPrintStream = null;
     
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

                //Save Evaluation Cmd Log
                String evalCmdAfterRun=absPathEval+splStrOS+"evalCmdAfterRun.txt";

                //Save Results
                String evalCmdBeforeRun =absPathEval+splStrOS+"evalCmdBeforeRun.sh";
                //String cmde =absPathEval+splStrOS+"evalCmdBeforeRun.sh";
                
                //[0] datasetName
                //[1] eval
                //[2] sequence
                //[3] cmd has 6 parameter. e.g. 'DETMeasure datasetName 01 3 >> evalCmdAfterRun.txt'
                String[][][][] mycmd = new String[datasetNamesList.size()][3][2][6];



                String cmdTotlStr= "";


                //Generate run cmd
/*                File myObj = new File(evalCmdAfterRun);
                if (myObj.createNewFile()) {
                    System.out.println("eval log created: " + myObj.getName());
                } else {
                    System.out.println("eval log already exists.");
                }*/

                 outputStream= new FileOutputStream(evalCmdBeforeRun);
                 outputPrintStream = new PrintStream(outputStream);

                for (int nameIdx = 0; nameIdx < datasetNamesList.size(); nameIdx++) {
                    for (int evalcmdIdx = 0; evalcmdIdx < 3; evalcmdIdx++){
                        for (int seqIdx = 0; seqIdx < 2; seqIdx++) {
                            cmdTotlStr="";
                            //parameter 00
                            mycmd[nameIdx][evalcmdIdx][seqIdx][0] = absPathEval + splStrOS + evalcmd[evalcmdIdx];
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][0]+" ";
                            //parameter 01
                            mycmd[nameIdx][evalcmdIdx][seqIdx][1] = absPathData + splStrOS + datasetNamesList.get(nameIdx);
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][1]+" ";
                            //parameter 02
                            mycmd[nameIdx][evalcmdIdx][seqIdx][2] = sequence[seqIdx];
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][2]+" ";
                            //parameter 03
                            mycmd[nameIdx][evalcmdIdx][seqIdx][3] = "3";
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][3]+" ";
                            //parameter 04
                            mycmd[nameIdx][evalcmdIdx][seqIdx][4] = ">>";
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][4]+" ";
                            //parameter 05
                            mycmd[nameIdx][evalcmdIdx][seqIdx][5] = evalCmdAfterRun;
                            cmdTotlStr += mycmd[nameIdx][evalcmdIdx][seqIdx][5];

                            //proc = run.exec(mycmd[nameIdx][evalcmdIdx], null, new File(absPathEval));
                            //cmdTotlStr = mycmd[nameIdx][0] + " " + mycmd[nameIdx][1] + " " + mycmd[nameIdx][2] + " " + mycmd[nameIdx][3];
                            System.out.println(cmdTotlStr);

                            if(EvalUtil.isWindows()){
                                outputPrintStream.print(cmdTotlStr+"\r\n");
                            }
                            else{
                                outputPrintStream.print(cmdTotlStr+"\n");
                            }

                            /*
                            // Run Cmd
                            //
                             */


                            //ProcessBuilder pb = new ProcessBuilder(cmdTotlStr);
                            //String cmdTest="ls -a >> "+absPathEval+splStrOS+"abc.txt";
//
//                            ProcessBuilder pb = new ProcessBuilder(cmdTest);
//                            Process p = pb.start();
//                            int exitCode = p.waitFor();




                            //Runtime.getRuntime().exec(cmdTest);
                            // Runtime.getRuntime().exec(cmdTotlStr);

                            //proc = run.exec(cmdTotlStr);
                            //proc = run.exec(mycmd[nameIdx][evalcmdIdx][seqIdx], null, new File(absPathEval));


//                            /*
//                            * Post Data Processing
//                            * */
//                            // Read Evaluation Log
//                            BufferedReader fileevalCmdAfterRunRead = new BufferedReader(new FileReader(evalCmdAfterRun));
//                            String lastLine = "";
//                            String currentLogLine="";
//                            //ArrayList<String> cmdlogList=new ArrayList<String>();
//                            while ((currentLogLine = fileevalCmdAfterRunRead.readLine()) != null) {
//                                System.out.println(currentLogLine);
//                                //cmdlogList.add(currentLogLine);
//                                lastLine=currentLogLine;
//                            }
//
//
//                            fileevalCmdAfterRunRead.close();
//
//                            //Save Result File
//                            //PrintStream fileSaveResult = new PrintStream(new FileOutputStream(evalCmdBeforeRun));
//                            mycmd[nameIdx][evalcmdIdx][seqIdx][0] = evalcmd[evalcmdIdx];
//                            mycmd[nameIdx][evalcmdIdx][seqIdx][1] = datasetNamesList.get(nameIdx);
//                            for (int parameterIndx=0;parameterIndx<4;parameterIndx++)
//                            {
//                                System.out.print(mycmd[nameIdx][evalcmdIdx][seqIdx][parameterIndx]);
//                                //if at end no comma printed
//                                System.out.print(",");
//
//                            }
//                            //FileUtils.writeStringToFile(fileSaveResult, "Hello File", forName("UTF-8"));
//                            System.out.print(lastLine);
//                            System.out.println("");

                        }
                    }
                }

                String[] cmdChown={"chown", "777",evalCmdBeforeRun};
                //String[] cmdEvalRun={ "/bin/bash", evalCmdBeforeRun};
                String[] cmdEvalRun={ "sh", evalCmdBeforeRun};

                Runtime rt = Runtime.getRuntime();
                rt.freeMemory();
                //rt.exec(cmdTotlStr);
                rt.exec(cmdChown);
                rt.exec(cmdEvalRun);

                //fileSaveResult.close();
                //extracted(datasetNamesList, evalcmd, evalCmdAfterRun, evalCmdBeforeRun, mycmd);
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
        }
    }


}