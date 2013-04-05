package com.folkol.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.folkol.model.Photo;

@RequestScoped
public class Photos {

   @PersistenceContext
   private EntityManager em;

   private List<Photo> photos;

   @Produces
   @Named
   public List<Photo> getPhotos() {
      return photos;
   }

   public void onMemberListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Photo member) {
      retrieveAllPhotosOrderedByName();
   }

   @PostConstruct
   public void retrieveAllPhotosOrderedByName() {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Photo> criteria = cb.createQuery(Photo.class);
      Root<Photo> photo = criteria.from(Photo.class);
      // Swap criteria statements if you would like to try out type-safe criteria queries, a new
      // feature in JPA 2.0
      // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
      criteria.select(photo).orderBy(cb.asc(photo.get("name")));
      photos = em.createQuery(criteria).getResultList();
   }
}
