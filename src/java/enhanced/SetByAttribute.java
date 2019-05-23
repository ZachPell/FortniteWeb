package enhanced;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SetByAttribute {
    AttributeType type();
}