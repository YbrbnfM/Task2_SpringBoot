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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@Table(name = "customers")
public class Customer {
	// @Column(name = "Id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@NotBlank(message = "Поле не может быть пустым")
	@Size(max = 300, message = "Предельный размер поля 300")
	@Column(name = "first_name")
	String firstName;
	@NotBlank(message = "Поле не может быть пустым")
	@Size(max = 300)
	@Column(name = "last_name")
	String lastName;
	@NotBlank(message = "Поле не может быть пустым")
	@Email(message = "Некорректный Email")
	@Size(max = 300, message = "Предельный размер поля 300")
	@Column(unique = true)
	String email;
	@NotBlank(message = "Поле не может быть пустым")
	// @Column(name = "Password")
	String password;
	@NotBlank(message = "Поле не может быть пустым")
	@Pattern(regexp = "\\+[0-9]+|[0-9]+", message = "Поле может содержать в себе только цифры")
	@Size(max = 50, min = 6, message = "Поле не может быть меньше 6 и больше 50 символов")
	@Column(name = "phone_number", unique = true)
	String phoneNumber;
	@NotNull(message = "Поле должно быть заполнено")
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "address_id")
	Address address;
	@NotNull(message = "paidTypes не может отсутствовать, введите значение [] чтобы оставить пустым")
	@ManyToMany
	@JoinTable(name = "customers_paid_types", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "paid_type_id"))
	List<PaidType> paidTypes;

	public List<PaidType> getPaidTypes() {
		return paidTypes;
	}
}
