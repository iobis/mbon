select coalesce(array_to_json(array_agg(a)), '[]')
from (
	select
		eov.id,
		name,
		(
			select row_to_json(n)
			from (
				select id, name
				from goos.parent
				where parent.id = eov.parent_id
			) n
		) as parent,
		(
			select array_to_json(array_agg(row_to_json(t)))
			from (
				select question.id, question.name
				from goos.eov_question
				left join goos.question on question.id = eov_question.question_id
				where eov_question.eov_id = eov.id
			) t
		) as questions,
		(
			select array_to_json(array_agg(row_to_json(t)))
			from (
				select requirement.id, requirement.name, requirement.type
				from goos.requirement_eov
				left join goos.requirement on requirement.id = requirement_eov.requirement_id
				where requirement_eov.eov_id = eov.id
			) t
		) as requirements
	from goos.eov
) a;
