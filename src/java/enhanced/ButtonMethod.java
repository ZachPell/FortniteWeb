package enhanced;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})

public @interface ButtonMethod {
    String buttonName() default "";
    boolean isDefault() default false;
}