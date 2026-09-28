package core;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

/**
 * Used to launch a jme application in desktop environment
 */
public class DesktopLauncher
{
    public static void main(String[] args)
    {
        AppSettings settings = new AppSettings(true);
        settings.setFullscreen(false);
        settings.setTitle("game");
        settings.setVSync(false);
        settings.setResizable(false);

        SimpleApplication app = new GameApplication();
        app.setSettings(settings);
        app.start();
    }
}
