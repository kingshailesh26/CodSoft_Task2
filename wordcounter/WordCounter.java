package wordcounter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WordCounter extends JFrame {
    private JTextArea textArea;
    private JLabel countLabel;
    private JTextArea uniqueWordsTextArea;

    public WordCounter() {
        super("Word Counter");

        // Create components
        textArea = new JTextArea(10, 30);
        JButton countButton = new JButton("Count");
        JButton browseButton = new JButton("Browse");
        countLabel = new JLabel("Word count: 0");
        uniqueWordsTextArea = new JTextArea(10, 30);

        // Set layout manager
        setLayout(new BorderLayout());

        // Create panel for button and label
        JPanel panel = new JPanel();
        panel.add(countButton);
        panel.add(browseButton);
        panel.add(countLabel);

        // Add components to the frame
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        add(new JScrollPane(uniqueWordsTextArea), BorderLayout.EAST);

        // Attach event listener to the count button
        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countWords();
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseFile();
            }
        });

        // Set frame properties and make it visible
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void countWords() {
        String text = textArea.getText();
        int wordCount = 0;
        Map<String, Integer> wordFrequency = new HashMap<>();

        if (!text.isEmpty()) {
            String[] words = text.trim().split("\\s+");
            wordCount = words.length;

            for (String word : words) {
                if (wordFrequency.containsKey(word)) {
                    int frequency = wordFrequency.get(word);
                    wordFrequency.put(word, frequency + 1);
                } else {
                    wordFrequency.put(word, 1);
                }
            }
        }

        countLabel.setText("Word count: " + wordCount);
        displayUniqueWords(wordFrequency);
    }

    private void displayUniqueWords(Map<String, Integer> wordFrequency) {
        uniqueWordsTextArea.setText("Unique Words:\n");

        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            String word = entry.getKey();
            int frequency = entry.getValue();
            uniqueWordsTextArea.append(word + " - " + frequency + "\n");
        }
    }

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));

        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                Scanner scanner = new Scanner(selectedFile);
                StringBuilder fileContent = new StringBuilder();
                while (scanner.hasNextLine()) {
                    fileContent.append(scanner.nextLine()).append("\n");
                }
                textArea.setText(fileContent.toString());
                scanner.close();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "File not found: " +
                        selectedFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WordCounter();
            }
        });
    }
}
