package core;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;
import com.jme3.anim.tween.action.BlendSpace;
import lombok.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record AnimComposerWrapper(AnimComposer composer)
{

    public ActionWrapper action(String name)
    {
        return action(name, composer.action(name));
    }

    public ActionWrapper action(String name, BlendSpace space, String... clips)
    {
        return action(name, composer.actionBlended(name, space, clips));
    }

    public ActionWrapper action(String name, Action action)
    {
        return new ActionWrapper(name, action);
    }

    public void setCurrentAction(ActionWrapper state)
    {
        composer.removeCurrentAction();
        if (composer.getCurrentAction() != state.action)
        {
            composer.setCurrentAction(state.name);
        }
    }

    @NullMarked
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ActionWrapper
    {
        private final String name;
        private final @Getter Action action;
    }

}
