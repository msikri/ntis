package org.apache.ntis.topology.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.config.YamlMapFactoryBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

@Component
public class TopologyInfoBean implements InitializingBean {

	private Table<String, String, Long> table;

	private final String[] propertylocation = new String[] { "classpath:topology.yml" };

	@Override
	public void afterPropertiesSet() throws Exception {

		Table<String, String, Long> tmpTable = HashBasedTable.create();

		List<Resource> resources = new ArrayList<Resource>();
		ResourceLoader loader = new DefaultResourceLoader();
		for (String loc : propertylocation) {
			resources.add(loader.getResource(loc));
		}

		YamlMapFactoryBean factory = new YamlMapFactoryBean();
		factory.setResources(resources.toArray(new Resource[resources.size()]));
		Map<String, Object> pro = factory.getObject();

		Object mapping = pro.get("topologysources");

		Assert.notNull(mapping);

		if (mapping instanceof List) {

			List values = (List) mapping;

			for (Object value : values) {

				if (value instanceof Map) {
					Map mappings = (Map) value;

					for (Object key : mappings.keySet()) {
						String source = (String) key;
						Object keyValue = mappings.get(key);
						if (keyValue instanceof Map) {

							Map datamap = (Map) keyValue;
							for (Object level2 : datamap.keySet()) {
								tmpTable.put(source, level2.toString(),
										((Integer) datamap.get(level2))
												.longValue());
							}

						} else {
							throw new Exception(
									"Cannot Complete Mapping for Second level Map");
						}
					}
				} else {
					throw new Exception("Cannot Complete Mapping for Map");
				}
			}

		} else {
			throw new Exception("Cannot Complete Mapping");
		}

		table = ArrayTable.create(tmpTable);
		System.out.println(tmpTable.size());

	}

	public long getDelayForTopology(String source, String destination) {
		return table.get(source, destination);
	}

}
