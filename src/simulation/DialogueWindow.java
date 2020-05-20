package simulation;

import javax.swing.*;

public class DialogueWindow extends JDialog{

    private JTextField gridSizeXField, gridSizeYField, pField, kField;
    private Display display;

    int gridSizeX, gridSizeY, k;
    float p;

    public DialogueWindow(Display parent){
        super(parent);
        display = parent;
        setLocationRelativeTo(parent);
        init();
    }

    private void init(){
        var pane = getContentPane();
        var layout = new GroupLayout(pane);
        pane.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        var gridSizeXLabel = new JLabel("Simulation width:");
        var gridSizeYLabel = new JLabel("Simulation width:");
        var pLabel = new JLabel("Mutation chance (p):");
        var kLabel = new JLabel("Cell lifetime (k):");

        gridSizeXField = new JTextField("10", 10);
        gridSizeYField = new JTextField("10", 10);
        pField = new JTextField("0.05", 10);
        kField = new JTextField("10", 10);

        var button = new JButton("OK");
        button.addActionListener(event -> buttonAction());

        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                .addComponent(gridSizeXLabel)
                .addComponent(gridSizeYLabel)
                .addComponent(pLabel)
                .addComponent(kLabel))
            .addGroup(layout.createParallelGroup()
                .addComponent(gridSizeXField)
                .addComponent(gridSizeYField)
                .addComponent(pField)
                .addComponent(kField)
                .addComponent(button))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(gridSizeXLabel)
                .addComponent(gridSizeXField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(gridSizeYLabel)
                .addComponent(gridSizeYField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(pLabel)
                .addComponent(pField))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(kLabel)
                .addComponent(kField))
            .addGroup(layout.createParallelGroup().addComponent(button))
        );

        add(button);

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void validateData() throws NumberFormatException, DataException{
        gridSizeX = Integer.parseInt(gridSizeXField.getText());
        gridSizeY = Integer.parseInt(gridSizeYField.getText());
        k = Integer.parseInt(kField.getText());
        p = Float.parseFloat(pField.getText());

        if(gridSizeX < 1 || gridSizeX > Application.blockPerSideLimit) throw new DataException();
        if(gridSizeY < 1 || gridSizeY > Application.blockPerSideLimit) throw new DataException();
        if(p < 0 || p > 1) throw new DataException();
        if(k < 1 ) throw new DataException();
    }

    private void buttonAction(){
        try{
            validateData();
            display.resetSimulationPanel(gridSizeX, gridSizeY, k, p);
            dispose();
        }
        catch (NumberFormatException e){
            showErrorMessage();
        }
        catch (DataException e){
            showErrorMessage();
        }
    }

    private void showErrorMessage(){
        JOptionPane.showMessageDialog(this, "Entered parameters are incorrect, please enter correct parameters", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private class DataException extends Exception{
        DataException(){
            super();
        }
    }
}
