package com.iktpreobuka.projekat_za_kraj.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;
import com.iktpreobuka.projekat_za_kraj.repositories.GradeRepository;

@Service
public class GradeDaoImpl implements GradeDao {
   
   @Autowired
   private GradeRepository gradeRepository;
   
   @Override
   public List<GradeEntity> findGradeValueByStudentAndSubject(Integer studentId, Integer subjectId) {
      return gradeRepository.findByStudentAndTeacherSubject(studentId, subjectId);
   }
   
  /* @Override
   public AccountEntity addDateOfActivationToAccount(Integer id, String doa) throws Exception {
      AccountEntity account = findAccountById(id);
      if (account == null) {
         throw new Exception();
      }
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      try {
         Date dateOfActivation = formatter.parse(doa);
         account.setAccountActivated(dateOfActivation);
      } catch (Exception e) {
         throw new Exception();
      }
      return account;
   }
   
   @Override
   public AccountEntity addAccKindToAccount(Integer id, String accKind) throws Exception {
      AccountEntity account = findAccountById(id);
      if (account == null) {
         throw new Exception();
      }
      try {
         for (EAccountKind kind : EAccountKind.values()) {
            if (kind.toString().equalsIgnoreCase(accKind)) {
               account.setAccountKind(kind);
            }
         }
      } catch (Exception e) {
         throw new Exception();
      }
      return account;
   } */
}