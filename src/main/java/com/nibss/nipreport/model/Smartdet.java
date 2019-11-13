/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nibss.nipreport.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author elixir
 */
public class Smartdet extends ArrayList<Smartdet.SmartdetEntry> {

    private String name;

    public Smartdet(int initialCapacity) {
        super(initialCapacity);
    }

    public Smartdet() {
    }

    public Smartdet(Collection<? extends SmartdetEntry> c) {
        super(c);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object clone() {
        // this shouldn't happen, since we are Cloneable
        Smartdet smartdet = new Smartdet();
        smartdet.name=this.name;
        for (SmartdetEntry entry : this) {
            SmartdetEntry e = entry;
            if (entry != null) {
                e = smartdet.new SmartdetEntry();
                e.accountNumber = entry.accountNumber;
                e.amount = entry.amount;
                e.date = entry.date;
                e.productCode = entry.productCode;
            }
            smartdet.add(e);
        }
        return smartdet;
    }

    public class SmartdetEntry {

        private String productCode;
        private String date;
        private String accountNumber;
        private BigDecimal amount = BigDecimal.ZERO;

        public SmartdetEntry() {
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

    }

}
