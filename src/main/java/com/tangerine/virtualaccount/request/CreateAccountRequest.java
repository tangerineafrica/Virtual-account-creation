package com.tangerine.virtualaccount.request;

import com.tangerine.virtualaccount.enums.Gender;
import com.tangerine.virtualaccount.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    private ProductType product_type;
    @NotBlank(message = "First name is mandatory")
    private String first_name;
    @NotBlank(message = "Last name is mandatory")
    private String last_name;
    @NotBlank(message = "Mobile number is mandatory")
    @Size(min = 11, max = 11)
    private String mobile_num;
    @NotBlank(message = "Email is mandatory")
    private String email;
    @NotBlank(message = "BVN is mandatory")
    @Size(min = 11, max = 11)
    private String bvn;
    @NotBlank(message = "Date of birth is mandatory")
    private String dob;
    @NotBlank(message = "Address is mandatory")
    private String address;
    @NotBlank(message = "Gender is mandatory")
    private Gender gender;
    @NotBlank(message = "Customer identifier is mandatory")
    private String customer_identifier;

}
