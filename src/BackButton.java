/**
 * Clase correspondiente a los botones de navegación entre ventanas.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 3.0.0
 *
 * @since 27/06/2021
 */

import javax.swing.JButton;
import javax.swing.JFrame;

public class BackButton extends JButton {

    /**
     * Constructor del botón para navegar hacia atrás entre ventanas.
     *
     * @param currentFrame  Ventana donde está colocado el botón.
     * @param previousFrame Ventana a la que regresar cuando se pulse el botón.
     */
    public BackButton(JFrame currentFrame, JFrame previousFrame) {
        setText("Atrás");
        setEnabled(true);
        setVisible(true);

        /*
         * Este método togglea la visibilidad de la ventana anterior,
         * eliminando la ventana actual para evitar múltiples
         * instancias de una misma ventana con distintas
         * visibilidades, ya que esto podría llevar a corrupción de
         * datos y a mantener información inutilizada en memoria.
         */
        addActionListener(e -> {
            previousFrame.setVisible(true);

            currentFrame.dispose();
        });
    }
}