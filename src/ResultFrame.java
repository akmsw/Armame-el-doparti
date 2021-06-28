/**
 * Clase correspondiente a la ventana de resultados
 * de distribución de jugadores.
 * 
 * @author Bonino, Francisco Ignacio.
 * 
 * @version 1.0.0
 * 
 * @since 06/03/2021
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ResultFrame extends JFrame {

    // Campos privados.
    private JPanel panel;
    private JFrame previousFrame;
    private JButton backButton;

    /**
     * Creación de la ventana de resultados.
     */
    public ResultFrame(JFrame previousFrame) {
        this.previousFrame = previousFrame;

        initializeComponents();

        if (InputFrame.distribution == 0)
            randomMix();
        else
            ratingMix();
    }

    // ----------------------------------------Métodos privados---------------------------------

    /**
     * Este método inicializa los componentes de la ventana de resultados.
     */
    private void initializeComponents() {
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(MainFrame.iconBall.getImage());

        backButton = new JButton("Atras");

        backButton.setBounds(0, 0, 100, 30);
        backButton.setEnabled(true);
        backButton.setVisible(true);
        backButton.addActionListener(new ActionListener() {
            /**
             * Este método togglea la visibilidad de las ventanas.
             * 
             * @param e Evento de click.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultFrame.this.setVisible(false);
                previousFrame.setVisible(true);
            }
        });

        panel = new JPanel(new MigLayout("wrap"));

        panel.add(backButton, "growx");
        
        add(panel);
    }

    /**
     * Este método se encarga de armar los equipos de manera completamente
     * aleatoria.
     */
    private void randomMix() {
        setTitle("MEZCLA ALEATORIA");
        setVisible(true);
    }

    /**
     * Este método se encarga de armar los equipos de la manera más
     * equitativa en base a las puntuaciones seteadas a los jugadores.
     */
    private void ratingMix() {
        setVisible(false);
        setTitle("MEZCLA POR PUNTAJES");

        RatingFrame ratingFrame = new RatingFrame(previousFrame);

        ratingFrame.setVisible(true);
    }
}