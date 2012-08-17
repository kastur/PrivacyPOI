from imposm.parser import OSMParser as P
from itertools import chain, groupby
import json

nested_nodes = []
p = P(concurrency=4, nodes_callback=lambda x: nested_nodes.append(x))

print 'Loading Los Angeles'
p.parse('los-angeles.osm.pbf')
nodes = list(chain.from_iterable(nested_nodes))
print 'Extracted %d nodes' % len(nodes)

amenities = [(tags, coord) for (id, tags, coord) in nodes if 'amenity' in tags]

sorted_amenities = sorted(amenities, key=lambda (tags,coord): tags['amenity'])
grouped_amenities = groupby(sorted_amenities, lambda (tags, coords): tags['amenity'])
types_of_amenities = [(k, len(list(v))) for (k, v) in grouped_amenities]
types_of_amenities = sorted(types_of_amenities, key=lambda (k, count): count, reverse=True)


grouped_amenities = groupby(sorted_amenities, lambda (tags, coords): tags['amenity'])
amenity_locations = [(k, [(lat, lon, ('name' in tags and tags['name'] or ''))  for (tags, (lat, lon)) in L]) for (k, L) in grouped_amenities]
amenity_locations = [(k, L) for (k, L) in amenity_locations if len(L) > 20]
amenity_locations = dict(amenity_locations)
serialized_data = json.dumps(amenity_locations)
f = open('los-angeles.amenities.json', 'w');
f.write(serialized_data);
f.close()

