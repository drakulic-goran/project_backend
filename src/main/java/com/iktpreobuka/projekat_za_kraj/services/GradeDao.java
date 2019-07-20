package com.iktpreobuka.projekat_za_kraj.services;

import java.util.List;

import com.iktpreobuka.projekat_za_kraj.entities.GradeEntity;

public interface GradeDao {
	
	public List<GradeEntity> findGradeValueByStudentAndSubject(Integer studentId, Integer subjectId);
	/* public GradeEntity addDateOfActivationToAccount(Integer id, String doa) throws Exception;
	public GradeEntity addAccKindToAccount(Integer id, String accKind) throws Exception; */
	

}
