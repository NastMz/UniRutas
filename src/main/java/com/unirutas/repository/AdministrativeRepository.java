package com.unirutas.repository;

import com.unirutas.core.builder.query.interfaces.ICustomQueryBuilder;
import com.unirutas.core.database.repository.CrudRepository;
import com.unirutas.core.dependency.annotations.Repository;
import com.unirutas.core.providers.CustomQueryBuilderProvider;
import com.unirutas.models.Administrative;
import com.unirutas.models.User;
import com.unirutas.core.builder.query.types.Tuple;

import java.util.List;

@Repository
public class AdministrativeRepository extends CrudRepository<Administrative> {
    public Administrative findByUsername(String username){
        ICustomQueryBuilder queryBuilder = CustomQueryBuilderProvider.getFactory().createCustomQueryBuilder(Administrative.class);
        List<List<Tuple<String, Object>>> consult = queryBuilder.select()
                .fields("name", "username", "password", "phone", "security_phrase")
                .where("username", username)
                .execute();
        User user = (User) consult.get(0).get(0).getValue();

        return (Administrative) user;
    }
}
