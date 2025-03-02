package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
public record GetWarehouseQuest(
      String name,
      String code,
      String location,
      Integer managerId,
      Boolean deleted
) { }
