CREATE TABLE esqui (_id integer primary key autoincrement, nombre text not null, cordillera text not null, n_remontes int not null, km_pistas float, fecha_ult_visita long, valoracion float, notas text);
INSERT INTO esqui VALUES(1,'Alto Campoo','Cantabrica',13,27, strftime('%s', '2011-02-25'),6,'Para un fin de semana');
INSERT INTO esqui VALUES(2,'Formigal','Pirineo Aragones',21,130,strftime('%s', '2012-09-15'),10,'Para una buena semana');
INSERT INTO esqui VALUES(3,'Baqueira','Pirineo Catalan',37,140,strftime('%s', '2013-01-26'),9,'Muy cara');
INSERT INTO esqui VALUES(4,'Navacerrada','Sistema Central',10,70,strftime('%s', '2008-12-25'),5,'Mucha gente');
CREATE UNIQUE INDEX nombre ON esqui(nombre ASC);