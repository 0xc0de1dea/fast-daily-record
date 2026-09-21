package com.example.chapter03daily.domain.daily.repository;

import com.example.chapter03daily.domain.daily.entity.Daily;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRepository extends JpaRepository<Daily, Long>, DailyCustomRepository {

}
