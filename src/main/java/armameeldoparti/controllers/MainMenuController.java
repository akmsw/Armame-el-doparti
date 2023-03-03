package armameeldoparti.controllers;

import armameeldoparti.models.Error;
import armameeldoparti.models.Views;
import armameeldoparti.utils.common.CommonFunctions;
import armameeldoparti.utils.common.Constants;
import armameeldoparti.views.MainMenuView;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.jetbrains.annotations.NotNull;

/**
 * Main menu view controller class.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 0.0.1
 *
 * @since 26/07/2022
 */
public class MainMenuController extends Controller {

  // ---------------------------------------- Constructor ---------------------------------------

  /**
   * Builds the main menu view controller.
   *
   * @param mainMenuView View to control.
   */
  public MainMenuController(@NotNull MainMenuView mainMenuView) {
    super(mainMenuView);
  }

  // ---------------------------------------- Public methods ------------------------------------

  /**
   * Makes the controlled view visible.
   */
  @Override
  public void showView() {
    centerView();
    getView().setVisible(true);
  }

  /**
   * 'Help' button event handler.
   *
   * <p>Makes the controlled view invisible
   * and shows the help view.
   */
  public void helpButtonEvent() {
    hideView();

    ((HelpController) CommonFunctions.getController(Views.HELP)).updatePage();

    CommonFunctions.getController(Views.HELP)
                   .showView();
  }

  /**
   * 'Start' button event handler.
   *
   * <p>Makes the controlled view invisible
   * and shows the names input view.
   */
  public void startButtonEvent() {
    hideView();

    CommonFunctions.getController(Views.NAMES_INPUT)
                   .showView();
  }

  /**
   * 'Contact' button event handler.
   *
   * <p>Opens the browser on the contact URL.
   */
  public void contactButtonEvent() {
    browserRedirect(Constants.URL_CONTACT);
  }

  /**
   * 'Reports & suggestions' button event handler.
   *
   * <p>Opens the browser on the issues URL.
   */
  public void issuesButtonEvent() {
    browserRedirect(Constants.URL_ISSUES);
  }

  // ---------------------------------------- Protected methods ---------------------------------

  /**
   * Resets the controlled view to its default values.
   */
  @Override
  protected void resetView() {
    // Not needed in this controller
  }

  // ---------------------------------------- Private methods -----------------------------------

  /**
   * Opens a new tab in the default web browser with the specified URL.
   *
   * @param link Destination URL.
   */
  private void browserRedirect(@NotNull String link) {
    try {
      Desktop.getDesktop()
             .browse(new URI(link));
    } catch (IOException | URISyntaxException e) {
      CommonFunctions.exitProgram(Error.BROWSER_ERROR);
    }
  }
}