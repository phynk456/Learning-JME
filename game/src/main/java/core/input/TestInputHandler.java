package core.input;

import org.jspecify.annotations.NullMarked;

import java.util.EventListener;

@NullMarked
public class TestInputHandler implements EventListener
{
    @InputHandler(keyName = "FORWARD")
    public void onW(boolean isPressed)
    {
        System.out.println("W is " + (isPressed ? "" : "not") + "pressed");
    }
}
