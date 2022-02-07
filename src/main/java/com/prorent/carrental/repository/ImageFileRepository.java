package com.prorent.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prorent.carrental.domain.ImageFile;

public interface ImageFileRepository extends JpaRepository<ImageFile, String> {

}
