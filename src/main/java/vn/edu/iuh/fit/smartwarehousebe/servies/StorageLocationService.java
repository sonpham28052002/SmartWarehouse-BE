package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.models.StorageLocation;
import vn.edu.iuh.fit.smartwarehousebe.repositories.InventoryRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StorageLocationRepository;

@Service
public class StorageLocationService {

    @Autowired
    private StorageLocationRepository storageLocationRepository;

    @Autowired
    private InventoryRepository inventoryRepository;



}


