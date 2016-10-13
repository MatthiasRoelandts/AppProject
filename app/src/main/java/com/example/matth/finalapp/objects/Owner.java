package com.example.matth.finalapp.objects;

import java.math.BigDecimal;

/**
 * Created by michael on 12/10/2016.
 */
public class Owner extends User{


    private BigDecimal total_earned;

    public BigDecimal getTotal_earned() {
        return total_earned;
    }

    public void setTotal_earned(BigDecimal total_earned) {
        this.total_earned = total_earned;
    }

    public Owner(String email, String password) {
        super(email, password);
    }


}
