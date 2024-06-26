package com.fullcycle.admin.catalogo.domain.entity.video;

import java.util.Optional;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;

public interface VideoGateway {
  
  Video create(Video video);

  Video update(Video video);

  void deletebyId(VideoID id);

  Optional<Video> findbyId(VideoID id);
  
  Pagination<Video> findAll(VideoSearchQuery query);
}