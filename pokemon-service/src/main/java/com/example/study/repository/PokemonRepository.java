package com.example.study.repository;

import com.example.study.model.PokemonModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokemonRepository extends MongoRepository<PokemonModel, Integer> {
  Optional<PokemonModel> findById(Integer id);
}
