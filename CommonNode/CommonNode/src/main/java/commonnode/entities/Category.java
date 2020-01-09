package commonnode.entities;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@Scope("prototype")
public class Category {
	int id;
	String name;
}
