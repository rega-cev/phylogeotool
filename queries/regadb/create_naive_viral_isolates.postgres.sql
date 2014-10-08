create view patient_minstartdate as
select patient_ii, MIN(start_date) as minstartdate
from regadbschema.patient join regadbschema.therapy using(patient_ii)
group by patient_ii;

create table naive_viral_isolates as
select patient_ii,patient_id,sample_id,viral_isolate_ii,sample_date,pds.dataset_ii,ds.description, nucleotides
from regadbschema.patient join regadbschema.viral_isolate vi using(patient_ii)
join regadbschema.nt_sequence nt using(viral_isolate_ii)
join regadbschema.patient_dataset pds using(patient_ii)
join regadbschema.dataset ds using(dataset_ii)
WHERE patient_ii NOT IN (select patient_ii from regadbschema.therapy)
UNION
select patient_ii,patient_id,sample_id,viral_isolate_ii,sample_date,pds.dataset_ii, ds.description, nucleotides
from patient_minstartdate join regadbschema.patient using(patient_ii)
join regadbschema.viral_isolate vi using(patient_ii)
join regadbschema.nt_sequence nt using(viral_isolate_ii)
join regadbschema.patient_dataset pds using(patient_ii)
join regadbschema.dataset ds using(dataset_ii)
WHERE (sample_date < minstartdate OR minstartdate is NULL) ;

drop view patient_minstartdate;
