package core.parkourgame.desktopmodule;

import core.GameApplication;

/**
 * Used to launch a jme application in desktop environment
 *
 */
public class DesktopLauncher
{
    void main()
    {
        GameApplication app = new GameApplication();
        app.start();
    }
}
