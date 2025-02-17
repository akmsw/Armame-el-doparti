package armameeldoparti.utils.common;

import armameeldoparti.controllers.Controller;
import armameeldoparti.models.Player;
import armameeldoparti.models.enums.Error;
import armameeldoparti.models.enums.Position;
import armameeldoparti.models.enums.ProgramView;
import armameeldoparti.views.View;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Common-use functions class.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 0.0.1
 *
 * @since 3.0
 */
public final class CommonFunctions {

  // ---------- Constructor --------------------------------------------------------------------------------------------------------------------------

  /**
   * Empty, private constructor.
   */
  private CommonFunctions() {
    // Body not needed
  }

  // ---------- Public methods -----------------------------------------------------------------------------------------------------------------------

  /**
   * Exits the program with the corresponding error message and error code according to the occurred exception.
   *
   * @param error The error that caused the program to end.
   */
  public static void exitProgram(Error error) {
    showMessage(
      null,
      Constants.MAP_ERROR_MESSAGE
               .get(error),
      JOptionPane.ERROR_MESSAGE
    );

    System.exit(Constants.MAP_ERROR_CODE
                         .get(error));
  }

  /**
   * Builds and displays a dialog window with a custom message.
   *
   * @param parentComponent   Graphical component where the dialog windows associated with the event should be displayed.
   * @param dialogMessage     Custom message to show.
   * @param dialogMessageType Message severity.
   */
  public static void showMessage(Component parentComponent, String dialogMessage, int dialogMessageType) {
    String dialogTitle = null;
    Icon dialogIcon = null;

    switch (dialogMessageType) {
      case JOptionPane.INFORMATION_MESSAGE, JOptionPane.PLAIN_MESSAGE -> {
        dialogTitle = Constants.TITLE_MESSAGE_INFORMATION;
        dialogIcon = Constants.ICON_DIALOG_INFORMATION;
      }
      case JOptionPane.WARNING_MESSAGE -> {
        dialogTitle = Constants.TITLE_MESSAGE_WARNING;
        dialogIcon = Constants.ICON_DIALOG_WARNING;
      }
      case JOptionPane.ERROR_MESSAGE -> {
        dialogTitle = Constants.TITLE_MESSAGE_ERROR;
        dialogIcon = Constants.ICON_DIALOG_ERROR;
      }
      case JOptionPane.QUESTION_MESSAGE -> {
        dialogTitle = Constants.TITLE_MESSAGE_QUESTION;
        dialogIcon = Constants.ICON_DIALOG_QUESTION;
      }
      default -> CommonFunctions.exitProgram(Error.ERROR_GUI);
    }

    JOptionPane.showMessageDialog(parentComponent, dialogMessage, dialogTitle, dialogMessageType, dialogIcon);
  }

  /**
   * Builds and displays a dialog window with options for the user to choose.
   * 
   * @param parentComponent Graphical component where the dialog windows associated with the event should be displayed.
   * @param dialogMessage   Custom message to show.
   * @param dialogOptions   Options for the user to choose.
   * 
   * @return The integer indicating the option chosen by the user.
   * 
   * @see JOptionPane#showOptionDialog(Component, Object, String, int, int, Icon, Object[], Object)
   */
  public static int showOptionDialog(Component parentComponent, String dialogMessage, Object[] dialogOptions) {
    return JOptionPane.showOptionDialog(
      parentComponent,
      dialogMessage,
      Constants.TITLE_MESSAGE_QUESTION,
      JOptionPane.OK_CANCEL_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      Constants.ICON_DIALOG_QUESTION,
      dialogOptions,
      dialogOptions[0]
    );
  }

  /**
   * Determines the monitor on which the majority of the given view is displayed and sets it as the active monitor.
   *
   * @param view Reference view from which the active monitor will be determined.
   */
  public static void updateActiveMonitorFromView(View view) {
    CommonFields.setActiveMonitor(
      retrieveOptional(
        Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment()
                                         .getScreenDevices())
              .filter(screen -> !screen.getDefaultConfiguration()
                                       .getBounds()
                                       .intersection(view.getBounds())
                                       .isEmpty())
              .max(Comparator.comparingDouble(
                  screen -> {
                    Rectangle intersection = screen.getDefaultConfiguration()
                                                   .getBounds()
                                                   .intersection(view.getBounds());

                    return intersection.getWidth() * intersection.getHeight();
                  }
                )
              )
      )
    );
  }

  /**
   * Opens a new tab in the default web browser with the specified URL.
   *
   * <p>The "java:S1190" warning is suppressed since JDK22+ allows the use of unnamed variables.
   *
   * @param url Destination URL.
   */
  @SuppressWarnings("java:S1190")
  public static void browserRedirect(String url) {
    try {
      Desktop.getDesktop()
             .browse(new URI(url));
    } catch (IOException | URISyntaxException _) {
      CommonFunctions.exitProgram(Error.ERROR_BROWSER);
    }
  }

  /**
   * Given an action event, this method returns the graphical component associated with it.
   *
   * @param e The triggered action event.
   *
   * @return The graphical component associated to the action event.
   */
  public static Component getComponentFromEvent(ActionEvent e) {
    return e == null ? null : SwingUtilities.windowForComponent((Component) e.getSource());
  }

  /**
   * Builds a string that represents the MiG layout constraints for the graphical component.
   *
   * @param constraints MiG Layout constraints for the component.
   *
   * @return The fully built component constraints.
   */
  public static String buildMigLayoutConstraints(String... constraints) {
    return String.join(", ", constraints);
  }

  /**
   * Capitalizes the first letter of the given string.
   *
   * @param input The string to capitalize.
   *
   * @return The given string with the first letter uppercase and the rest lowercase.
   */
  public static String capitalize(String input) {
    return input.isBlank() ? input : input.substring(0, 1)
                                          .toUpperCase()
                                     + input.substring(1)
                                            .toLowerCase();
  }

  /**
   * Given an image filename, creates an ImageIcon with it.
   *
   * @param imageFileName Name of the image file.
   *
   * @return The ImageIcon of the specified file.
   */
  public static ImageIcon createImage(String imageFileName) {
    return Objects.requireNonNull(new ImageIcon(Constants.class
                                                         .getClassLoader()
                                                         .getResource(Constants.PATH_IMG + imageFileName)));
  }

  /**
   * Given an icon filename, appends the icons folder path to it and creates an ImageIcon.
   *
   * @param iconFileName Name of the icon file to use.
   *
   * @return The ImageIcon of the specified file.
   *
   * @see #createImage(String)
   */
  public static ImageIcon createImageIcon(String iconFileName) {
    return createImage(Constants.PATH_ICO + iconFileName);
  }

  /**
   * Scales an icon to the specified width and height.
   *
   * @param icon   Icon to scale.
   * @param width  New width.
   * @param height New height.
   * @param hints  Scaling method.
   *
   * @return The scaled icon.
   */
  public static ImageIcon scaleImageIcon(ImageIcon icon, int width, int height, int hints) {
    return new ImageIcon(icon.getImage()
                             .getScaledInstance(width, height, hints));
  }

  /**
   * Gets the corresponding controller to the requested view.
   *
   * <p>The "java:S1452" warning is suppressed since the Java compiler can't know at runtime the type of the controlled view.
   *
   * @param view The view whose controller is needed.
   *
   * @return The requested view's controller.
   */
  @SuppressWarnings("java:S1452")
  public static Controller<? extends View> getController(ProgramView view) {
    return CommonFields.getControllersMap()
                       .get(view);
  }

  /**
   * Gets a list containing the anchored players grouped by their anchorage number.
   *
   * @return A list containing the anchored players grouped by their anchorage number.
  */
  public static List<List<Player>> getAnchorages() {
    return new ArrayList<>(CommonFields.getPlayersSets()
                                       .values()
                                       .stream()
                                       .flatMap(List::stream)
                                       .filter(Player::isAnchored)
                                       .collect(Collectors.groupingBy(Player::getAnchorageNumber))
                                       .values());
  }

  /**
   * Checks if an optional that should not be null has a value present. If so, that value is retrieved. If the optional has no value, then the program
   * exits with a fatal internal error code.
   *
   * @param <T>      Generic optional type.
   * @param optional The optional to be checked.
   *
   * @return The optional value if present.
   */
  public static <T> T retrieveOptional(Optional<T> optional) {
    if (!optional.isPresent()) {
      exitProgram(Error.ERROR_INTERNAL);
    }

    return optional.get();
  }

  /**
   * Gets the search-corresponding position in a generic map received.
   *
   * @param <T>    Generic value type.
   * @param map    Generic map with positions as keys.
   * @param search Value to search in the map.
   *
   * @return The search-corresponding position.
   */
  public static <T> Position getCorrespondingPosition(Map<Position, T> map, T search) {
    return retrieveOptional(map.entrySet()
                               .stream()
                               .filter(entry -> entry.getValue()
                                                     .equals(search))
                               .map(Map.Entry::getKey)
                               .findFirst());
  }
}