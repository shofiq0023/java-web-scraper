package com.shofiqul.scrapper.serviceimpl;

import com.shofiqul.scrapper.model.IconInformation;
import com.shofiqul.scrapper.model.PlayStoreIconsEntity;
import com.shofiqul.scrapper.model.PlayStoreIconDto;
import com.shofiqul.scrapper.repo.PlayStoreIconsRepo;
import com.shofiqul.scrapper.service.FileService;
import com.shofiqul.scrapper.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScraperServiceImpl implements ScraperService {
	@Value("${playStore}")
	String playStoreUrl;

	private final PlayStoreIconsRepo repo;
	private final FileService fileService;

	@Override
	public PlayStoreIconDto getPlayStoreData() {
		PlayStoreIconDto response = new PlayStoreIconDto();
		extractPlayStoreData(response, playStoreUrl);

		if (response.getItems().size() > 0) {
			Set<PlayStoreIconsEntity> datas = new HashSet<PlayStoreIconsEntity>();
			for (IconInformation item : response.getItems()) {
				datas.add(copyProperties(item, PlayStoreIconsEntity.class));
			}

			repo.saveAll(datas);
		}

		return response;
	}

	private void extractPlayStoreData(PlayStoreIconDto response, String url) {
		try {
			// Get the full html document
			Document doc = Jsoup.connect(url).get();
			Element section = doc.getElementsByTag("section").first();

			Set<IconInformation> appSet = new HashSet<IconInformation>();
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
					IconInformation app = new IconInformation();
					Element image = listItem.getElementsByTag("img").first();

					// Set the app image
					if (image != null) {
						app.setImageUrl(image.attr("src"));
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

	@Override
	public boolean createFilesFromData() {
		Set<PlayStoreIconsEntity> entities = new HashSet<PlayStoreIconsEntity>(repo.findAll());

		if (entities.size() > 0) {
			for(PlayStoreIconsEntity entity : entities) {
				fileService.createWebpFile(entity);
			}

			return true;
		}

		return false;
	}
}