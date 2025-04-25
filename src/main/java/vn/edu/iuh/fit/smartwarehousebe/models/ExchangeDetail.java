package vn.edu.iuh.fit.smartwarehousebe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ExchangeDetail {

  @Id
  private String id;

  @ManyToOne
  private Exchange exchange;

  @ManyToOne
  private Transaction transaction;


}
