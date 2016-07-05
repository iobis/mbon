select row_to_json(fc) 
from (
	select 'FeatureCollection' as type,
	array_to_json(array_agg(f)) as features
	from (
		select 'Feature' as type,
		ST_AsGeoJSON(lg.coverage)::json As geometry,
		row_to_json(
			(
				select l from (select id) as l 
			)
		) as properties
		from goos.activity As lg
	) as f
) as fc;