package core.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@NullMarked
@RequiredArgsConstructor
public final class InputActionAppState extends AbstractAppState implements ActionListener
{
    private final HashMap<String, ExecutableObject> keyListenerMap = new HashMap<>();
    @Nullable private InputManager input;

    @Override
    public void onAction(String name, boolean isPressed, float tpf)
    {
        if (keyListenerMap.containsKey(name))
        {
            try
            {
                keyListenerMap.get(name).execute(isPressed);
            }
            catch (InvocationTargetException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app)
    {
        super.initialize(stateManager, app);
        updateInputManager(app.getInputManager());
    }

    public void updateInputManager(final InputManager newInput)
    {
        if (input != null)
        {
            input.clearMappings();
            input.removeListener(this);
        }

        input = newInput;
        for (MovementInputKey key : MovementInputKey.values())
        {
            String name = key.name();
            for (KeyTrigger trigger : key.triggers)
            {
                input.addMapping(key.name(), trigger);
            }
            input.addListener(this, name);
        }
    }

    public void registerListener(EventListener listener)
    {
        for (Method method : listener.getClass().getDeclaredMethods())
        {
            if (method.isAnnotationPresent(InputHandler.class))
            {
                if (method.getParameterCount() != 1 || !method.getParameters()[0].getType().equals(boolean.class))
                {
                    throw new IllegalArgumentException();
                }

                String key = method.getAnnotation(InputHandler.class).keyName();

                if (keyListenerMap.containsKey(key))
                {
                    keyListenerMap.get(key).methods.add(method);
                }
                else
                {
                    keyListenerMap.put(key, new ExecutableObject(listener, List.of(method)));
                }
            }
        }
    }

    @NullMarked
    @RequiredArgsConstructor
    private static class ExecutableObject
    {

        private final EventListener listener;
        private final List<Method> methods;

        private void execute(Object... args) throws InvocationTargetException, IllegalAccessException
        {
            for (Method method : methods)
            {
                method.invoke(listener, args);
            }
        }
    }
}
