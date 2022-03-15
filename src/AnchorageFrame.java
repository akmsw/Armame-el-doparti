/**
 * Clase correspondiente a la ventana de anclaje de jugadores.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 3.0.0
 *
 * @since 15/03/2021
 */

import java.awt.Font;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

public class AnchorageFrame extends JFrame {

    /* ---------------------------------------- Constantes privadas ------------------------------ */

    private static final String GROWX_SPAN = "growx, span";
    private static final String FRAME_TITLE = "Anclaje de jugadores";

    /* ---------------------------------------- Campos privados ---------------------------------- */

    private int maxPlayersPerAnchorage;
    private int anchorageNum;
    private int playersAnchored;

    private ArrayList<ArrayList<JCheckBox>> cbSets;

    private ArrayList<JCheckBox> cdCB;
    private ArrayList<JCheckBox> ldCB;
    private ArrayList<JCheckBox> mfCB;
    private ArrayList<JCheckBox> fwCB;
    private ArrayList<JCheckBox> gkCB;

    private JButton newAnchorageButton;
    private JButton clearAnchoragesButton;
    private JButton deleteAnchorageButton;
    private JButton deleteLastAnchorageButton;

    private JPanel masterPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;

    private JScrollPane scrollPane;

    private JTextArea textArea;

    private InputFrame inputFrame;

    /**
     * Creación de la ventana de anclajes.
     *
     * @param inputFrame    Ventana cuya visibilidad será toggleada.
     * @param playersAmount Cantidad de jugadores por equipo.
     */
    public AnchorageFrame(InputFrame inputFrame, int playersAmount) {
        this.inputFrame = inputFrame;

        maxPlayersPerAnchorage = playersAmount - 1;

        masterPanel = new JPanel(new MigLayout("wrap 2"));
        leftPanel = new JPanel(new MigLayout("wrap 2"));
        rightPanel = new JPanel(new MigLayout("wrap"));

        cdCB = new ArrayList<>();
        ldCB = new ArrayList<>();
        mfCB = new ArrayList<>();
        fwCB = new ArrayList<>();
        gkCB = new ArrayList<>();

        cbSets = new ArrayList<>();

        cbSets.add(cdCB);
        cbSets.add(ldCB);
        cbSets.add(mfCB);
        cbSets.add(fwCB);
        cbSets.add(gkCB);

        anchorageNum = 0;
        playersAnchored = 0;

        initializeComponents();
    }

    /* ---------------------------------------- Métodos privados --------------------------------- */

    /**
     * Este método se encarga de inicializar los
     * componentes de la ventana de anclaje.
     */
    private void initializeComponents() {
        textArea = new JTextArea();

        scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        int index = 0;

        for (Map.Entry<Position, Player[]> ps : inputFrame.getPlayersMap().entrySet()) {
            fillCBSet(ps.getValue(), cbSets.get(index));
            addCBSet(leftPanel, cbSets.get(index), Main.positions.get(Position.values()[index]));

            index++;
        }

        leftPanel.setBackground(Main.FRAMES_BG_COLOR);

        textArea.setBorder(BorderFactory.createBevelBorder(1));
        textArea.setEditable(false);
        textArea.setVisible(true);

        rightPanel.setBackground(Main.FRAMES_BG_COLOR);

        addButtons();

        masterPanel.add(leftPanel, "west");
        masterPanel.add(rightPanel, "east");

        masterPanel.setBackground(Main.FRAMES_BG_COLOR);

        updateTextArea();

        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(MainFrame.icon.getImage());

        add(masterPanel);

        pack();

        setResizable(false);
        setLocationRelativeTo(null);
    }

    /**
     * Este método se encarga de colocar en los paneles de la ventan los botones.
     */
    private void addButtons() {
        JButton okButton = new JButton("Finalizar");
        BackButton backButton = new BackButton(AnchorageFrame.this, inputFrame);

        newAnchorageButton = new JButton("Anclar");
        deleteAnchorageButton = new JButton("Borrar un anclaje");
        deleteLastAnchorageButton = new JButton("Borrar último anclaje");
        clearAnchoragesButton = new JButton("Limpiar anclajes");

        /*
         * Este método hace invisible la ventana de anclaje cuando el usuario hizo los
         * anclajes deseados y está listo para distribuir los jugadores.
         * Se crea, además, la ventana de resultados. Se destildan aquellos checkboxes
         * que hayan quedado seleccionados sin anclarse.
         */
        okButton.addActionListener(e -> {

            cbSets.forEach(cbs -> cbs.forEach(cb -> {
                if (cb.isSelected() && cb.isVisible())
                    cb.setSelected(false);
            }));

            /*
             * Si la distribución es por puntajes, antes de mostrar
             * la ventana de resultados, se crea la ventana de ingreso
             * de puntajes para los jugadores.
             */
            if (inputFrame.getDistribution() == 1) {
                RatingFrame ratingFrame = new RatingFrame(inputFrame, AnchorageFrame.this);

                ratingFrame.setVisible(true);
            } else {
                ResultFrame resultFrame = new ResultFrame(inputFrame, AnchorageFrame.this);

                resultFrame.setVisible(true);
            }

            AnchorageFrame.this.setVisible(false);
        });

        /*
         * Este método togglea la visibilidad de las ventanas.
         * Se sobreescribe para eliminar todos los anclajes
         * hechos en caso de querer retroceder.
         */
        backButton.addActionListener(e -> {
            clearAnchorages();

            inputFrame.setVisible(true);

            AnchorageFrame.this.dispose();
        });

        /*
         * Este método se encarga de anclar los jugadores cuya checkbox está tildada.
         * Sólo se permitirán hacer anclajes de no menos de 2 y no más de maxPlayersPerAnchorage
         * jugadores.
         * No se podrán hacer anclajes que tengan más de la mitad de jugadores de
         * una misma posición.
         */
        newAnchorageButton.addActionListener(e -> {
            int anchored = 0;

            for (ArrayList<JCheckBox> cbSet : cbSets)
                for (JCheckBox cb : cbSet)
                    if (cb.isSelected())
                        anchored++;

            if (!validChecksAmount(anchored)) {
                errorMsg("No puede haber más de " + maxPlayersPerAnchorage + " ni menos de 2 jugadores en un mismo anclaje");

                return;
            } else if (!isValidAnchorage()) {
                errorMsg("No puede haber más de la mitad de jugadores de una misma posición en un mismo anclaje");

                return;
            } else if (!validAnchorageAmount(anchored)) {
                errorMsg("No puede haber más de " + (2 * maxPlayersPerAnchorage) + " jugadores anclados en total");

                return;
            }

            anchorageNum++;

            for (int i = 0; i < cbSets.size(); i++)
                setAnchors(cbSets.get(i), inputFrame.getPlayersMap().get(Position.values()[i]));

            updateTextArea();

            inputFrame.setTotalAnchorages(anchorageNum);
        });

        /*
         * Este método se encarga de borrar un anclaje en específico señalado por el
         * usuario.
         */
        deleteAnchorageButton.addActionListener(e -> {
            String[] optionsDelete = new String[anchorageNum];

            for (int i = 0; i < anchorageNum; i++)
                optionsDelete[i] = Integer.toString(i + 1);

            int anchor = JOptionPane.showOptionDialog(null, "Seleccione qué anclaje desea borrar",
                    "Antes de continuar...", 2, JOptionPane.QUESTION_MESSAGE, MainFrame.smallIcon,
                    optionsDelete, optionsDelete[0]) + 1; // + 1 para compensar índice del arreglo

            if ((anchor - 1) != JOptionPane.CLOSED_OPTION) {
                // Los que tenían anclaje igual a 'anchor' ahora tienen anclaje '0'
                for (int j = 0; j < cbSets.size(); j++)
                    changeAnchor(inputFrame.getPlayersMap().get(Position.values()[j]), cbSets.get(j), anchor, 0);

                /*
                 * A los que tienen anclaje desde 'anchor + 1' hasta 'anchorageNum'
                 * se les decrementa en 1 su número de anclaje.
                 */
                for (int k = (anchor + 1); k <= anchorageNum; k++)
                    for (int j = 0; j < cbSets.size(); j++)
                        changeAnchor(inputFrame.getPlayersMap().get(Position.values()[j]), cbSets.get(j), k, (k - 1));

                anchorageNum--;

                updateTextArea();
            }
        });

        // Este método se encarga de borrar el último anclaje realizado
        deleteLastAnchorageButton.addActionListener(e -> deleteLast());

        // Este método se encarga de borrar todos los anclajes que se hayan generado
        clearAnchoragesButton.addActionListener(e -> clearAnchorages());

        leftPanel.add(okButton, GROWX_SPAN);
        leftPanel.add(backButton, GROWX_SPAN);

        rightPanel.add(scrollPane, "span2, push, grow");
        rightPanel.add(newAnchorageButton, "grow");
        rightPanel.add(deleteAnchorageButton, "grow");
        rightPanel.add(deleteLastAnchorageButton, "grow");
        rightPanel.add(clearAnchoragesButton, "grow");
    }

    /**
     * Este método se encarga de borrar todos los anclajes que se hayan generado.
     */
    private void clearAnchorages() {
        do {
            deleteLast();
        } while (anchorageNum > 0);
    }

    /**
     * Este método se encarga de llenar los arreglos de checkboxes correspondientes
     * a cada posición.
     *
     * @param playersSet Conjunto de jugadores de donde obtener los nombres.
     * @param cbSet      Conjunto de checkboxes a llenar.
     */
    private void fillCBSet(Player[] playersSet, ArrayList<JCheckBox> cbSet) {
        for (Player player : playersSet)
            cbSet.add(new JCheckBox(player.getName()));
    }

    /**
     * Este método se encarga de colocar en el panel los checkboxes correspondientes
     * a cada posición junto con una etiqueta que los distinga.
     *
     * @param panel Panel donde se colocarán los checkboxes.
     * @param cbSet Conjunto de checkboxes a colocar.
     * @param title Texto de la etiqueta de acompañamiento.
     */
    private void addCBSet(JPanel panel, ArrayList<JCheckBox> cbSet, String title) {
        JLabel label = new JLabel(title);
        JSeparator line = new JSeparator(SwingConstants.HORIZONTAL);

        label.setFont(Main.getProgramFont().deriveFont(Font.BOLD));

        panel.add(label, "span");

        for (JCheckBox cb : cbSet)
            panel.add(cb, "align left, pushx");

        panel.add(line, GROWX_SPAN);
    }

    /**
     * @return Si la cantidad de jugadores anclados es al menos 2 y
     *         no más de maxPlayersPerAnchorage.
     *
     * @param anchored Cantidad de jugadores que se intenta anclar.
     */
    private boolean validChecksAmount(int anchored) {
        return ((anchored <= maxPlayersPerAnchorage) && (anchored >= 2));
    }

    /**
     * @return Si el anclaje no posee más de la mitad de algún conjunto de
     *         jugadores.
     */
    private boolean isValidAnchorage() {
        for (ArrayList<JCheckBox> cbSet : cbSets) {
            int anchor = 0;

            for (JCheckBox cb : cbSet)
                if (cb.isSelected())
                    anchor++;

            if (anchor > (cbSet.size() / 2))
                return false;
        }

        return true;
    }

    /**
     * @return Si la cantidad de jugadores anclados en total no supera el
     *         máximo permitido.
     *
     * @param playersToAnchor Cantidad de jugadores que se intenta anclar.
     */
    private boolean validAnchorageAmount(int playersToAnchor) {
        return ((playersAnchored + playersToAnchor) <= (2 * maxPlayersPerAnchorage));
    }

    /**
     * Este método se encarga de crear una ventana de error con un texto
     * personalizado.
     *
     * @param errMsg Mensaje de error a mostrar en la ventana.
     */
    private void errorMsg(String errMsg) {
        JOptionPane.showMessageDialog(null, errMsg, "¡Error!", JOptionPane.ERROR_MESSAGE, null);
    }

    /**
     * Este método se encarga de setear el número de anclaje correspondiente a cada
     * jugador. Luego, se deseleccionan estas checkboxes y se las hace invisibles
     * para evitar que dos o más anclajes contengan uno o más jugadores iguales. En
     * caso de que el campo 'anchorageNum' sea 0 (se han querido limpiar los
     * anclajes), se resetearán los números de anclaje de cada jugador y todas las
     * checkboxes quedarán visibles y deseleccionadas.
     *
     * @param cbSet Arreglo de checkboxes a chequear.
     * @param pSet  Arreglo de jugadores correspondiente al arreglo de checkboxes.
     */
    private void setAnchors(ArrayList<JCheckBox> cbSet, Player[] pSet) {
        for (Player player : pSet)
            for (JCheckBox cb : cbSet) {
                if (anchorageNum != 0) {
                    if (cb.getText().equals(player.getName()) && cb.isSelected()) {
                        player.setAnchor(anchorageNum);

                        playersAnchored++;

                        cb.setSelected(false);
                        cb.setVisible(false);
                    }
                } else {
                    player.setAnchor(anchorageNum);

                    cb.setSelected(false);
                    cb.setVisible(true);
                }
            }
    }

    /**
     * Este método se encarga de borrar el último anclaje realizado.
     */
    private void deleteLast() {
        for (int i = 0; i < cbSets.size(); i++)
            changeAnchor(inputFrame.getPlayersMap().get(Position.values()[i]), cbSets.get(i), anchorageNum, 0);

        anchorageNum--;

        updateTextArea();
    }

    /**
     * Este método se encarga de actualizar el área de texto mostrando la cantidad
     * de anclajes y los jugadores anclados a los mismos.
     */
    private void updateTextArea() {
        textArea.setText("");

        for (int i = 1; i <= anchorageNum; i++) {
            int counter = 1;

            textArea.append(" ----- ANCLAJE #" + i + " -----\n");

            for (Map.Entry<Position, Player[]> ps : inputFrame.getPlayersMap().entrySet())
                for (Player player : ps.getValue())
                    if (player.getAnchor() == i) {
                        textArea.append(" " + counter + ". " + player.getName() + "\n");
                        counter++;
                    }

            textArea.append("\n");
        }

        toggleButtons();
    }

    /**
     * Este método se encarga de togglear los botones del panel
     * derecho de la ventana y los checkboxes del panel izquierdo
     * de la ventana.
     */
    private void toggleButtons() {
        if (anchorageNum > 0 && anchorageNum < 2) {
            deleteAnchorageButton.setEnabled(false);
            deleteLastAnchorageButton.setEnabled(true);
            clearAnchoragesButton.setEnabled(true);
        } else if (anchorageNum >= 2) {
            deleteAnchorageButton.setEnabled(true);
            deleteLastAnchorageButton.setEnabled(true);
            clearAnchoragesButton.setEnabled(true);
        } else {
            deleteAnchorageButton.setEnabled(false);
            deleteLastAnchorageButton.setEnabled(false);
            clearAnchoragesButton.setEnabled(false);
        }

        if (((2 * maxPlayersPerAnchorage) - playersAnchored) < 2) {
            newAnchorageButton.setEnabled(false);

            cbSets.forEach(cbs -> cbs.forEach(cb -> cb.setEnabled(!cb.isEnabled())));
        } else {
            newAnchorageButton.setEnabled(true);

            cbSets.forEach(cbs -> cbs.forEach(cb -> {
                if (!cb.isEnabled() && !cb.isSelected())
                    cb.setEnabled(true);
            }));
        }
    }

    /**
     * Este método se encarga de cambiar el número de anclaje de los jugadores.
     *
     * @param playersSet  Conjunto de jugadores a recorrer.
     * @param cbSet       Conjunto de checkboxes a recorrer.
     * @param target      Anclaje a reemplazar.
     * @param replacement Nuevo anclaje a setear.
     */
    private void changeAnchor(Player[] playersSet, ArrayList<JCheckBox> cbSet, int target, int replacement) {
        for (JCheckBox cb : cbSet)
            for (Player player : playersSet)
                if (cb.getText().equals(player.getName()) && (player.getAnchor() == target)) {
                    player.setAnchor(replacement);

                    if (replacement == 0) {
                        cb.setVisible(true);

                        playersAnchored--;
                    }
                }
    }

    /* ---------------------------------------- Métodos públicos --------------------------------- */

    /**
     * @return La cantidad de anclajes hechos.
     */
    public int getAnchoragesAmount() {
        return anchorageNum;
    }
}