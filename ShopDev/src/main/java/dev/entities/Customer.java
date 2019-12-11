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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "PaidType")
public class Customer {
	@Setter(value = AccessLevel.PRIVATE)	
	@Column(name = "Id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@NotBlank
	@Size(max = 300)
	@Column(name = "First_Name")
	String firstName;
	@NotBlank
	@Size(max = 300)
	@Column(name = "Last_Name")
	String lastName;
	@NotBlank
	@Email
	@Size(max = 300)
	@Column(name = "Email", unique = true)
	String email;
	@NonNull
	@Column(name = "Password")
	String password;
	@NotBlank
	@Pattern(regexp = "[0-9]+")
	@Size(max = 50)
	@Column(name = "Phone_Number", unique = true)
	String phoneNumber;
	/*
	 * TODO: пересмотреть в сторону связи многие к одному. Текущая реализация
	 * выполнена для соответсвия ТЗ и выполнения каскадного удаления адреса при
	 * удалении пользователя
	 */
	// @ManyToOne
	@NonNull
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "AddressId")
	Address address;
	// TODO: Вероятно неправильное поведение. Проверить
	@NonNull
	@ManyToMany
	@JoinTable(name = "CustomersPaidTypes", joinColumns = @JoinColumn(name = "CustomerId"), inverseJoinColumns = @JoinColumn(name = "PaidTypeId"))
	List<PaidType> paidTypes;
}
