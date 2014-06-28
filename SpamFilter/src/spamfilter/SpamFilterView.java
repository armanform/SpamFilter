/*
 * SpamFilterView.java
 */

package spamfilter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.learning.spamclassifier.Sigmoid;
import org.text.process.TextPreProcess;
/**
 * The application's main frame.
 */
public class SpamFilterView extends FrameView {

    public SpamFilterView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SpamFilterApp.getApplication().getMainFrame();
            aboutBox = new SpamFilterAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SpamFilterApp.getApplication().show(aboutBox);
    }

    private Scanner selectTextFile()
    {
        do {
             JFileChooser chooser = new JFileChooser();
             FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text/Java files", "txt", "java");
             chooser.setFileFilter(filter);
             int returnVal = chooser.showOpenDialog(null);
             try {
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                       return new Scanner(chooser.getSelectedFile());
                } 
                else {
                       return null;
                }
              }
              catch (FileNotFoundException e) {
                   JOptionPane.showMessageDialog(null, "Invalid file!",
                   "error", JOptionPane.ERROR_MESSAGE); 
              }
	} while (true);
    }
    
    @Action
    public void OpenButtonPressed() throws FileNotFoundException, IOException 
    {
        Scanner lineScanner = selectTextFile();
	String emailInput = "";
        String toOutput = "";
	if (lineScanner!=null) {
	   while (lineScanner.hasNextLine()) {
               String thisLine = lineScanner.nextLine();
               emailInput += thisLine + " ";
               toOutput += thisLine + "\n";
           }
	}
        MainTextArea.setText(toOutput);
        int[] featureVector = TextPreProcess.processEmail(emailInput);
        
        double isSpam = predictSpam(featureVector);
        
        float probability = Sigmoid.sigmoid(isSpam);
        
        JOptionPane.showMessageDialog(mainPanel, "The probability that this email is spam: " + probability);
        
    }
    
    private double predictSpam(int[] featureVector) throws FileNotFoundException, IOException
    {
        double p = 0;
        BufferedReader br1 = new BufferedReader(new FileReader((new File(".")).getAbsolutePath() + "\\TrainData\\weights.txt"));
        String line1;
        int i1 = 0;
        double[] weights = new double[1900];
        while ((line1 = br1.readLine()) != null) {
            try{
                weights[i1] = Double.parseDouble(line1);
            }catch(Exception e)
            {
            }
            if(i1 == 0)
                p += weights[i1] * 1;
            else
                p += weights[i1] * featureVector[i1 - 1];
            i1++;
        }
        br1.close();
        return p;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        MainTxtArea = new javax.swing.JScrollPane();
        MainTextArea = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        OpenItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        process = new java.awt.Button();

        mainPanel.setName("mainPanel"); // NOI18N

        MainTxtArea.setName("MainTxtArea"); // NOI18N

        MainTextArea.setColumns(20);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(spamfilter.SpamFilterApp.class).getContext().getResourceMap(SpamFilterView.class);
        MainTextArea.setFont(resourceMap.getFont("MainTextArea.font")); // NOI18N
        MainTextArea.setRows(5);
        MainTextArea.setName("MainTextArea"); // NOI18N
        MainTxtArea.setViewportView(MainTextArea);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainTxtArea, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainTxtArea, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(spamfilter.SpamFilterApp.class).getContext().getActionMap(SpamFilterView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        OpenItem.setAction(actionMap.get("OpenButtonPressed")); // NOI18N
        OpenItem.setText(resourceMap.getString("OpenItem.text")); // NOI18N
        OpenItem.setName("OpenItem"); // NOI18N
        fileMenu.add(OpenItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        process.setLabel(resourceMap.getString("process.label")); // NOI18N
        process.setName("process"); // NOI18N
        process.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statusMessageLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(process, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(process, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

private void processActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processActionPerformed
// TODO add your handling code here:
	String emailInput = "";
        emailInput =  MainTextArea.getText();
        if(emailInput.isEmpty() || emailInput.length() < 10 || emailInput == null)
            return;
        int[] featureVector;
        double isSpam = 0.0d;
        try {
            featureVector = TextPreProcess.processEmail(emailInput);
            isSpam = predictSpam(featureVector);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SpamFilterView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SpamFilterView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        float probability = Sigmoid.sigmoid(isSpam);
        
        JOptionPane.showMessageDialog(mainPanel, "The probability that this email is spam: " + probability);
}//GEN-LAST:event_processActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea MainTextArea;
    private javax.swing.JScrollPane MainTxtArea;
    private javax.swing.JMenuItem OpenItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private java.awt.Button process;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
