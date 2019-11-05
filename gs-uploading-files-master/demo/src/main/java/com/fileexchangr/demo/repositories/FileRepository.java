package com.fileexchangr.demo.repositories;

import com.fileexchangr.demo.entitys.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository  extends JpaRepository<DBFile, Long> {
    DBFile findByName(String name);
}
