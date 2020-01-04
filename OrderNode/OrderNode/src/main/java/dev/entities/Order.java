package dev.entities;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@Column(name = "offer_id")
	@Positive(message = "Указан неверный offer")
	int offerId;
	@NotBlank(message = "Поле не может быть пустым")
	@Size(max = 300, message = "Предельный размер поля 300")
	String name;
	@Column(name = "delivery_time")
	Timestamp deliveryTime;
	@NotNull(message = "Поле должно быть заполнено")
	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinColumn(name = "status_id")
	Status status;
	@Column(name = "customer_id")
	@Positive(message = "Указан неверный customer")
	int customerId;
	boolean paid;
}
