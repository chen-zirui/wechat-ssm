package cn.qs.service.impl.wechat;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qs.bean.wechat.Pay;
import cn.qs.mapper.BaseMapper;
import cn.qs.mapper.wechat.PayMapper;
import cn.qs.mapper.wechat.custom.PayCustomMapper;
import cn.qs.service.impl.AbastractBaseSequenceServiceImpl;
import cn.qs.service.wechat.PayService;
import cn.qs.utils.DefaultValue;
import cn.qs.utils.system.SystemUtils;

@Service
@Transactional
public class PayServiceImpl extends AbastractBaseSequenceServiceImpl<Pay> implements PayService {

	@Autowired
	private PayMapper payMapper;

	@Autowired
	private PayCustomMapper payCustomMapper;

	@Override
	public BaseMapper<Pay, Integer> getBaseMapper() {
		return payMapper;
	}

	@Override
	public Map<String, Object> detail(Integer id) {
		return payCustomMapper.detail(id);
	}

	@Override
	public List<Pay> listByCondition(Map condition) {
		// 普通用户只能查自己创建的
		if (DefaultValue.ROLE_SYSYEM.equals(SystemUtils.getLoginUser().getRoles())) {
			if (StringUtils.isNotBlank(MapUtils.getString(condition, "keywords"))) {
				return payCustomMapper.listByCondition(condition);
			}

			return payMapper.findAll();
		}

		condition.put("creator", SystemUtils.getLoginUser().getUsername());
		return payCustomMapper.listByConditionByCreator(condition);
	}
}
