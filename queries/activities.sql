select coalesce(array_to_json(array_agg(a)), '[]')
from (
	select
		activity.id,
		ST_AsText(ST_Envelope(coverage)) as coverage,
		timescale,
		(
			select row_to_json(n)
			from (
				select id, name
				from goos.network
				where network.id = activity.network_id
			) n
		) as network,
		(
			select row_to_json(n)
			from (
				select id, name
				from goos.eov
				where eov.id = activity.eov_id
			) n
		) as eov,
		(
			select array_to_json(array_agg(row_to_json(t)))
			from (
				select tool.id, tool.name
				from goos.activity_tool
				left join goos.tool on tool.id = activity_tool.tool_id
				where activity_tool.activity_id = activity.id
			) t
		) as tools,
		(
			select array_to_json(array_agg(row_to_json(t)))
			from (
				select dataproduct.id, dataproduct.name
				from goos.activity_dataproduct
				left join goos.dataproduct on dataproduct.id = activity_dataproduct.dataproduct_id
				where activity_dataproduct.activity_id = activity.id
			) t
		) as dataproducts,
		(
			select array_to_json(array_agg(row_to_json(t)))
			from (
				select datasystem.id, datasystem.name
				from goos.activity_datasystem
				left join goos.datasystem on datasystem.id = activity_datasystem.datasystem_id
				where activity_datasystem.activity_id = activity.id
			) t
		) as datasystems

	from goos.activity
) a;
