package com.unirutas.repository;

import com.unirutas.core.database.repository.CrudRepository;
import com.unirutas.core.dependency.annotations.Repository;
import com.unirutas.models.StudentAlert;

@Repository
public class StudentAlertRepository extends CrudRepository<StudentAlert> {
}
