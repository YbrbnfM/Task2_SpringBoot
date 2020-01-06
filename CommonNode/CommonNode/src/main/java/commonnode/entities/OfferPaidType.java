package commonnode.entities;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
@Scope(value = "prototype")
public class OfferPaidType {
	int id;
	int paidTypeId;
}
