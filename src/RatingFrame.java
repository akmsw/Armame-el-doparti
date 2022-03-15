/**
 * Clase correspondiente a la ventana de ingreso
 * de puntaje de jugadores.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 3.0.0
 *
 * @since 06/03/2021
 */

import java.awt.Font;

import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

public class RatingFrame extends JFrame {

    /* ---------------------------------------- Constantes privadas ------------------------------ */

    private static final String GROW_SPAN = "grow, span";

    /* ---------------------------------------- Campos privados ---------------------------------- */

    private ResultFrame resultFrame;

    /**
     * Creación de la ventana de ingreso de puntajes.
     *
     * @param inputFrame    Ventana de ingreso de datos, de la cual se obtendrá
     *                      información importante.
     * @param previousFrame Ventana fuente que crea la ventana RatingFrame.
     */
    public RatingFrame(InputFrame inputFrame, JFrame previousFrame) {
        JPanel panel = new JPanel(new MigLayout());

        JButton finishButton = new JButton("Finalizar");
        JButton resetButton = new JButton("Reiniciar puntajes");

        HashMap<Player, JSpinner> spinnersMap = new HashMap<>();

        BackButton backButton = new BackButton(RatingFrame.this, previousFrame);

        panel.setBackground(Main.FRAMES_BG_COLOR);

        // Este método envía togglear la visibilidad de las ventanas
        finishButton.addActionListener(e -> {
            spinnersMap.forEach((k, v) -> k.setRating((int) v.getValue()));

            resultFrame = new ResultFrame(inputFrame, RatingFrame.this);

            resultFrame.setVisible(true);

            RatingFrame.this.setVisible(false);
        });

        // Este método se encarga de resetear las puntuaciones de los jugadores
        resetButton.addActionListener(e -> spinnersMap.forEach((k, v) -> { v.setValue(1); k.setRating(0); }));

        for (int i = 0; i < inputFrame.getPlayersMap().size(); i++) {
            JLabel label = new JLabel(Main.positions.get(Position.values()[i]));

            label.setFont(Main.getProgramFont().deriveFont(Font.BOLD));

            panel.add(label, "span");
            panel.add(new JSeparator(SwingConstants.HORIZONTAL), "growx, span");

            for (int j = 0; j < inputFrame.getPlayersMap().get(Position.values()[i]).length; j++) {
                spinnersMap.put(inputFrame.getPlayersMap().get(Position.values()[i])[j],
                                new JSpinner(new SpinnerNumberModel(1, 1, 5, 1)));

                panel.add(new JLabel(inputFrame.getPlayersMap().get(Position.values()[i])[j].getName()), "pushx");

                if ((j % 2) != 0)
                    panel.add(spinnersMap.get(inputFrame.getPlayersMap().get(Position.values()[i])[j]), "wrap");
                else
                    panel.add(spinnersMap.get(inputFrame.getPlayersMap().get(Position.values()[i])[j]));
            }
        }

        panel.add(finishButton, GROW_SPAN);
        panel.add(resetButton, GROW_SPAN);
        panel.add(backButton, GROW_SPAN);

        add(panel);

        setTitle("Puntuaciones");
        setIconImage(MainFrame.icon.getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();

        setResizable(false);
        setLocationRelativeTo(null);
    }
}