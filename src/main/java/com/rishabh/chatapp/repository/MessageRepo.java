package com.rishabh.chatapp.repository;

import com.rishabh.chatapp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    @Query("""
            select m from Message m where((m.fromUserId = ?1 and m.toUserId = ?2) or (m.fromUserId = ?2 and m.toUserId = ?1))and m.sentAt >= :time
            order by m.sentAt asc
            """)
    List<Message> getHistory(UUID uid, UUID fid, @Param("time") Timestamp time);

}
