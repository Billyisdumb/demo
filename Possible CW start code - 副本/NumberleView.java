import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Observer;
import java.awt.Color;

import static java.awt.Color.*;

public class NumberleView implements Observer {
    String[] operatorSymbols = {"+", "-", "*", "/", "="};
    String[][] history;
    private int numRows = 0;
    private int numCols = 0;

    private final INumberleModel model;
    private final NumberleController controller;
    private final JFrame frame = new JFrame("Numberle");
    private final JTextField inputTextField = new JTextField(10);;
    private final JLabel attemptsLabel = new JLabel("Attempts remaining: ");
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final TableCellColorChanger colorChanger = new TableCellColorChanger(); // 创建 TableCellColorChanger 实例

    Font font = new Font("Arial", Font.BOLD, 22); // 使用 Arial 字体，粗体，大小为 16


    public NumberleView(INumberleModel model, NumberleController controller) {
        this.controller = controller;
        this.model = model;
        this.controller.startNewGame();
        ((NumberleModel) this.model).addObserver(this);
        tableModel = new DefaultTableModel(6, 7);
        table = new JTable(tableModel);
        initializeFrame();
        this.controller.setView(this);
        update((NumberleModel) this.model, null);

    }
    public class TableCellColorChanger {
        public void changeCellColor(JTable table, int targetRow, int targetColumn, Color color) {
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (row == targetRow && column == targetColumn) {
                        component.setBackground(color);
                    } else {
                        // 设置其他单元格的背景色为默认颜色
                        component.setBackground(table.getBackground());
                    }
                    return component;
                }
            };
            table.getColumnModel().getColumn(targetColumn).setCellRenderer(renderer);
            table.repaint();
        }
    }
    public void setColor(String currentGuess) {
        String targetNumber = controller.getTargetWord();
        Color[] colors = controller.getColors(targetNumber, currentGuess);
        for (int i = 0; i < targetNumber.length(); i++) {
            colorChanger.changeCellColor(table, numRows, i, colors[i]);
        }
    }

    private String getCharactersFromTable(int row, int startColumn, int endColumn) {
        StringBuilder characters = new StringBuilder();
        for (int column = startColumn; column <= endColumn; column++) {
            Object value = tableModel.getValueAt(row, column);
            if (value != null) {
                characters.append(value);
            }
        }
        return characters.toString();
    }

    public void initializeFrame() {
        int cellSize = 60; // 每个单元格的大小（宽度和高度相同）
        table.setRowHeight(cellSize);
        table.setFont(font);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());


        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(new JPanel());


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1));
        inputPanel.add(inputTextField);
        attemptsLabel.setText("Attempts remaining: " + controller.getRemainingAttempts());
        inputPanel.add(attemptsLabel);
        center.add(inputPanel);
        center.add(new JPanel());
        frame.add(center, BorderLayout.NORTH);
        center.add(new JScrollPane(table));

        //操作区
        JPanel keyboardPanel = new JPanel(new GridLayout(2, 1));
        keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.Y_AXIS)); // 垂直排列
        keyboardPanel.setPreferredSize(new Dimension(100, 100));

        // 第一排
        JPanel numberPanel = new JPanel(new GridLayout(1, 10)); // numberPanel 布局
        for (int i = 0; i < 10; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setEnabled(true);
            button.addActionListener(e -> {
                inputTextField.setText(inputTextField.getText() + button.getText());
                tableModel.setValueAt(button.getText(), numRows, numCols);
                numCols++;
            });
            button.setPreferredSize(new Dimension(25, 25));
            numberPanel.add(button);
            button.setFont(font);
        }
        keyboardPanel.add(numberPanel);
        // 第二排
        JPanel operatorPanel = new JPanel(new GridLayout(1, 6)); // operatorPanel 布局
        JButton DeleteButton = new JButton("Delete");
        DeleteButton.addActionListener(e -> {
            inputTextField.setText("");
            for (int col = 0; col <= 6; col++) { // 假设要删除第1到第6列的所有值
                tableModel.setValueAt("", numRows, col);
            }
            numCols = 0;
        });
        operatorPanel.add(DeleteButton);
        for (String symbol : operatorSymbols) {
            JButton symbolbutton = new JButton(symbol);
            symbolbutton.setEnabled(true);
            symbolbutton.addActionListener(e -> {
                inputTextField.setText(inputTextField.getText() + symbol);
                tableModel.setValueAt(symbol, numRows, numCols);
                numCols++;
            });
            symbolbutton.setPreferredSize(new Dimension(25, 25));
            operatorPanel.add(symbolbutton);
            symbolbutton.setFont(font);
        }

        JButton submitButton = new JButton("Enter");
        submitButton.addActionListener(e -> {
            String characters = getCharactersFromTable(numRows, 0, 6);
            controller.getCurrentGuess().setLength(0);
            controller.getCurrentGuess().append(characters);
            controller.processInput(inputTextField.getText());
            inputTextField.setText("");
            numCols = 0;
            numRows++;
        });
        operatorPanel.add(submitButton);
        keyboardPanel.add(operatorPanel);

        keyboardPanel.add(new JPanel());
        frame.add(keyboardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        submitButton.setFont(font);
        DeleteButton.setFont(font);
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        attemptsLabel.setText("Attempts remaining: " + controller.getRemainingAttempts());
        String text = inputTextField.getText();
        try {
            controller.isMean(text);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
//        setColor(text);
        if (controller.isGameWon()) {
            JOptionPane.showMessageDialog(null, "Congratulations! You have won the game!");
            System.exit(0);
        }
        if (controller.isGameOver()) {
            JOptionPane.showMessageDialog(null, "Sorry, You loss the game");
            System.exit(0);
        }
        setColor(text);
    }
}