package com.unirutas.repository;

import com.flexcore.database.repository.CrudRepository;
import com.flexcore.dependency.annotations.Repository;
import com.unirutas.models.StudentAlert;

@Repository
public class StudentAlertRepository extends CrudRepository<StudentAlert> {
}
