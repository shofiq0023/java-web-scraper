package com.shofiqul.scrapper.serviceimpl;

import com.shofiqul.scrapper.model.ApplicationItems;
import com.shofiqul.scrapper.model.PlayStoreApplicationModel;
import com.shofiqul.scrapper.model.PlayStoreDto;
import com.shofiqul.scrapper.model.ResponseDto;
import com.shofiqul.scrapper.repo.PlayStoreApplicationRepo;
import com.shofiqul.scrapper.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScraperServiceImpl implements ScraperService {
	@Value("#{'${website}'.split(',')}")
	List<String> urls;

	@Value("${playStore}")
	String playStoreUrl;

	private final PlayStoreApplicationRepo repo;

	@Override
	public Set<ResponseDto> getVehicleByModel(String vehicleModel) {
		Set<ResponseDto> response = new HashSet<ResponseDto>();

		for (String url : urls) {
			if (url.contains("ikman")) {
				System.out.println(url + vehicleModel);
				extractDataFromIkman(response, url + vehicleModel);
			}
		}
		return response;
	}

	private void extractDataFromIkman(Set<ResponseDto> dtos, String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Element element = doc.getElementsByClass("list--3NxGO").first();
			Elements elements = element.getElementsByTag("a");

			for (Element ads : elements) {
				ResponseDto dto = new ResponseDto();

				if (StringUtils.isNotEmpty(ads.attr("href"))) {
					dto.setTitle(ads.attr("title"));
					dto.setUrl("https://ikman.lk" + ads.attr("href"));
				}

				if (dto.getUrl() != null) {
					dtos.add(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public PlayStoreDto getPlayStoreData() {
		PlayStoreDto response = new PlayStoreDto();
		extractPlayStoreData(response, playStoreUrl);

		if (response.getItems().size() > 0) {
			Set<PlayStoreApplicationModel> datas = new HashSet<PlayStoreApplicationModel>();
			for (ApplicationItems item : response.getItems()) {
				datas.add(copyProperties(item, PlayStoreApplicationModel.class));
			}

			repo.saveAll(datas);
		}

		return response;
	}

	private void extractPlayStoreData(PlayStoreDto response, String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Element section = doc.getElementsByTag("section").first();

			Set<ApplicationItems> appSet = new HashSet<ApplicationItems>();
			Element header = section.getElementsByClass("oVnAB").first();

			// Set the Heading title
			if (header != null) {
				Element subHeader = header.getElementsByClass("kcen6d ").first();
				Element title = subHeader.getElementsByTag("span").first();
				response.setSectionTitle(title.text());
			}

			// Get the list of the apps
			Element scrollList = section.getElementsByClass("aoJE7e b0ZfVe").first();
			if (scrollList != null) {
				Elements list = scrollList.getElementsByClass("ULeU3b neq64b");

				for (Element listItem : list) {
					ApplicationItems app = new ApplicationItems();
					Element image = listItem.getElementsByTag("img").first();

					// Set the app image
					if (image != null) {
						app.setImage(image.attr("src"));
					}

					// Set the app title
					Element title = listItem.getElementsByClass("Epkrse ").first();
					if (title != null) {
						app.setTitle(title.text());
					} else {
						// Look for another title if first one was not found
						Element anotherTitle = listItem.getElementsByClass("sT93pb DdYX5 OnEJge ").first();
						if (anotherTitle != null) {
							app.setTitle(anotherTitle.text());
						}
					}

					// Set the app rating
					Element rating = listItem.getElementsByClass("LrNMN").first();
					if (rating != null) {
						app.setRating(rating.text().substring(0, 3));
					}

					appSet.add(app);
				}
			}

			response.setItems(appSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private <T> T copyProperties(Object source, Class<T> clazz) {
		T classInstance = null;

		try {
			classInstance = clazz.getDeclaredConstructor().newInstance();

			BeanUtils.copyProperties(source, classInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return classInstance;
	}
}