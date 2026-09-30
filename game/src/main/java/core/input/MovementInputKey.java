package core.input;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public enum MovementInputKey
{
    BACKWARD(KeyInput.KEY_S),
    FORWARD(KeyInput.KEY_W),
    RIGHT(KeyInput.KEY_D),
    LEFT(KeyInput.KEY_A),
    ;

    final List<KeyTrigger> triggers;

    MovementInputKey(int... codes)
    {
        ArrayList<KeyTrigger> mutableList = new ArrayList<>();
        for (int code : codes)
        {
            mutableList.add(new KeyTrigger(code));
        }
        triggers = mutableList;
    }
}
