-- CAI 747880 - Personalització de les enquestes
alter table GUS_USUARIENC add(USE_DNI varchar2 (10));
alter table GUS_ENCVOT modify(vot_input varchar2(2000));
alter table GUS_ENCUST add(ENC_IDENTIF varchar2(1));