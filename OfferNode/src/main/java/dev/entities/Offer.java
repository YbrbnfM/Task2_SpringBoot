package dev.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "offers")
public class Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@NotBlank(message = "Поле не может быть пустым")
	@Size(max = 300, message = "Предельный размер поля 300")
	String name;
	@Positive(message = "Цена может иметь только положительное значение")
	float price;
	@Column(name = "paid_type_id")
	@Positive(message = "Указан неверный тип оплаты")
	int paidTypeId;
	@NotNull(message = "Поле должно быть заполнено")
	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	@JoinColumn(name = "category_id")
	Category category;
	@NotNull(message = "characteristics не может отсутствовать, введите значение [] чтобы оставить пустым")
	@ManyToMany
	@JoinTable(name = "characterisics_offers", joinColumns = @JoinColumn(name = "offer_id"), inverseJoinColumns = @JoinColumn(name = "characteristic_id"))
	List<Characteristic> characteristics;
}
