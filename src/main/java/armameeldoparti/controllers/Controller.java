package armameeldoparti.controllers;

import armameeldoparti.utils.common.CommonFields;
import armameeldoparti.utils.common.CommonFunctions;
import armameeldoparti.views.View;
import java.awt.Rectangle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class that specifies the basic methods for
 * interaction between controllers and their assigned views.
 *
 * @author Bonino, Francisco Ignacio.
 *
 * @version 0.0.1
 *
 * @since 28/07/2022
 */
public abstract class Controller {

  // ---------------------------------------- Private fields ------------------------------------

  private @Getter @Setter(AccessLevel.PROTECTED) View view;

  // ---------------------------------------- Constructor ---------------------------------------

  /**
   * Builds the view controller.
   *
   * @param view View to control.
   */
  protected Controller(@NotNull View view) {
    setView(view);
  }

  // ---------------------------------------- Protected methods ---------------------------------

  /**
   * Centers the controlled view on the current active monitor.
   */
  protected void centerView() {
    Rectangle activeMonitorBounds = CommonFields.getActiveMonitor()
                                                .getDefaultConfiguration()
                                                .getBounds();

    getView().setLocation((activeMonitorBounds.width - getView().getWidth()) / 2 + activeMonitorBounds.x, 
                          (activeMonitorBounds.height - getView().getHeight()) / 2 + activeMonitorBounds.y);
  }

  /**
   * Makes the controlled view invisible.
   */
  protected void hideView() {
    getView().setVisible(false);

    CommonFunctions.updateActiveMonitorFromView(view);
  }

  /**
   * Makes the controlled view visible.
   */
  protected void showView() {
    centerView();
    getView().setVisible(true);
  }

  // ---------------------------------------- Abstract protected methods ------------------------

  /**
   * Resets the controlled view to its default values.
   */
  protected abstract void resetView();
}