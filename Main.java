import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends JFrame {
    private JLabel totalVotersLabel;
    private JButton voteBRSButton, voteBJPButton, voteCongressButton, notaButton, exitButton;
    private int votesBRS, votesBJP, votesCongress, votesNota, totalVoters;

    public Main() {
        setTitle("Voting Machine");
        setSize(400,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 10, 10));

        totalVotersLabel = new JLabel("Total Voters: ");

        voteBRSButton = new JButton("BRS");
        voteBJPButton = new JButton("BJP");
        voteCongressButton = new JButton("CONGRESS");
        notaButton = new JButton("NOTA");
        exitButton = new JButton("Exit Voting");

        mainPanel.add(totalVotersLabel);
        mainPanel.add(new JLabel()); // Empty label for spacing
        mainPanel.add(voteBRSButton);
        mainPanel.add(voteBJPButton);
        mainPanel.add(voteCongressButton);
        mainPanel.add(notaButton);
        mainPanel.add(exitButton);

        add(mainPanel);

        addActionListeners();
    }

    private void addActionListeners() {
        voteBRSButton.addActionListener(e -> castVote("BRS"));
        voteBJPButton.addActionListener(e -> castVote("BJP"));
        voteCongressButton.addActionListener(e -> castVote("CONGRESS"));
        notaButton.addActionListener(e -> castVote("NOTA"));
        exitButton.addActionListener(e -> endVoting());
    }

    private void castVote(String party) {
        switch (party) {
            case "BRS":
                votesBRS++;
                break;
            case "BJP":
                votesBJP++;
                break;
            case "CONGRESS":
                votesCongress++;
                break;
            case "NOTA":
                votesNota++;
                break;
        }
        checkTotalVotes();
    }

    private void checkTotalVotes() {
        if (votesBRS + votesBJP + votesCongress + votesNota >= totalVoters) {
            endVoting();
        } else {
            updateTotalVotersLabel();
        }
    }

    private void updateTotalVotersLabel() {
        totalVotersLabel.setText("Total Voters: " + (votesBRS + votesBJP + votesCongress + votesNota) + " / " + totalVoters);
    }

    private String calculateResults() {
        double percentageBRS = ((double) votesBRS / totalVoters) * 100;
        double percentageBJP = ((double) votesBJP / totalVoters) * 100;
        double percentageCongress = ((double) votesCongress / totalVoters) * 100;
        double percentageNota = ((double) votesNota / totalVoters) * 100;

        String winner;
        if (votesBRS >= votesBJP && votesBRS >= votesCongress && votesBRS >= votesNota) {
            winner = "BRS";
        } else if (votesBJP >= votesCongress && votesBJP >= votesNota) {
            winner = "BJP";
        } else if (votesCongress >= votesNota) {
            winner = "CONGRESS";
        } else {
            winner = "NOTA";
        }

        return String.format("Results:\nBRS - %d votes (%.2f%%)\nBJP - %d votes (%.2f%%)\nCONGRESS - %d votes (%.2f%%)\nNOTA - %d votes (%.2f%%)\nTotal Votes: %d\nWinner: %s",
                votesBRS, percentageBRS, votesBJP, percentageBJP, votesCongress, percentageCongress, votesNota, percentageNota,
                votesBRS + votesBJP + votesCongress + votesNota, winner);
    }

    private void endVoting() {
        String results = calculateResults();

        try (FileWriter writer = new FileWriter("voting_results.txt")) {
            writer.write(results);
            JOptionPane.showMessageDialog(this, "Voting has ended. Results have been written to voting_results.txt. Thank you for participating!");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error writing results to file. Please check the file permissions.");
        } finally {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main votingMachine = new Main();
            votingMachine.totalVoters = Integer.parseInt(JOptionPane.showInputDialog("Enter total voters:"));
            votingMachine.setVisible(true);
        });
    }
}
